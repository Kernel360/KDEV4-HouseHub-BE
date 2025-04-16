package com.househub.backend.domain.property.service.impl;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.AgentStatus;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.property.dto.*;
import com.househub.backend.domain.property.dto.propertyCondition.PropertyConditionReqDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.repository.PropertyRepository;
import com.househub.backend.domain.property.service.PropertyConditionService;
import com.househub.backend.domain.property.service.PropertyService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

	private final PropertyRepository propertyRepository;
	private final CustomerRepository customerRepository;
	private final AgentRepository agentRepository;
	private final PropertyConditionService propertyConditionService;

	/**
	 * 매물 등록
	 * @param dto 매물 등록 정보 DTO
	 * @param agentId 공인중개사 ID
	 */
	@Transactional
	@Override
	public void createProperty(PropertyReqDto dto, Long agentId) {
		// 로그인한 공인중개사 조회
		Agent agent = findAgentById(agentId);
		// 의뢰인(임대인 또는 매도인) 존재 여부 확인
		Customer customer = findCustomerById(dto.getCustomerId());
		// 매물을 등록할 수 있는 조건인지 확인
		validateRegisterProperty(dto.getRoadAddress(), dto.getDetailAddress(), dto.getCustomerId());
		// dto -> entity
		Property property = dto.toEntity(customer, agent);
		// db에 매물 저장
		propertyRepository.save(property);
		// db에 매물 조건 저장
		if(dto.getConditions() != null) {
			for (PropertyConditionReqDto conditionReqDto : dto.getConditions()) {
				propertyConditionService.createPropertyCondition(property.getId(), conditionReqDto);
			}
		}
	}

	/**
	 * 매물 상세 조회 - 해당 매물의 계약 리스트도 함께 조회
	 * @param id 매물 ID
	 * @return 매물 상세 정보 응답 DTO
	 */
	@Transactional(readOnly = true)
	@Override
	public FindPropertyDetailResDto findProperty(Long id) {
		// 매물 조회
		Property property = findPropertyById(id);
		// entity -> dto
		// 해당 매물의 조건, 계약 리스트도 response 에 포함
		FindPropertyDetailResDto response = FindPropertyDetailResDto.fromEntity(property);
		return response;
	}

	/**
	 * 매물 전체 조회 (검색 포함)
	 * @param searchDto 매물 검색 조건 DTO
	 * @param pageable 페이지네이션 정보
	 * @return 매물 기본 정보 응답 DTO LIST
	 */
	@Transactional(readOnly = true)
	@Override
	public PropertyListResDto findProperties(PropertySearchDto searchDto, Pageable pageable, Long agentId) {
		// 페이지네이션, 검색 필터링 적용하여 매물 조회
		Page<Property> propertyList = propertyRepository.searchProperties(
			agentId,
			searchDto.getProvince(),
			searchDto.getCity(),
			searchDto.getDong(),
			searchDto.getPropertyType(),
			searchDto.getAgentName(),
			searchDto.getCustomerName(),
			searchDto.getActive(),
			pageable
		);
		// 매물 엔티티를 dto 로 변환하여 리스트로 반환
		return PropertyListResDto.fromPage(propertyList.map(FindPropertyResDto::toDto));
	}

	/**
	 * 매물 정보 수정
	 * @param propertyId 매물 id
	 * @param updateDto 수정된 매물 정보 DTO
	 */
	@Transactional
	@Override
	public void updateProperty(Long propertyId, PropertyUpdateReqDto updateDto) {
		// 매물 조회
		Property property = findPropertyById(propertyId);

		// 주소 관련 필드 미리 계산
		String roadAddress = updateDto.getRoadAddress() != null ? updateDto.getRoadAddress() : property.getRoadAddress();
		String detailAddress = updateDto.getDetailAddress() != null ? updateDto.getDetailAddress() : property.getDetailAddress();
		Long customerId = updateDto.getCustomerId() != null ? updateDto.getCustomerId() : property.getCustomer().getId();

		// 주소 유효성 검사
		validateRegisterProperty(roadAddress, detailAddress, customerId);

		// 의뢰인(임대인 또는 매도인) 변경 여부 확인 후, 있으면 변경
		if(updateDto.getCustomerId() != null) {
			Customer customer = findCustomerById(updateDto.getCustomerId());
			property.changeCustomer(customer);
		}
		// id로 조회한 매물 정보 수정 및 저장
		property.updateProperty(updateDto);
	}

	/**
	 * 매물 삭제
	 * @param propertyId 매물 ID
	 */
	@Transactional
	@Override
	public void deleteProperty(Long propertyId) {
		// 매물 조회
		Property property = findPropertyById(propertyId);
		// 매물 소프트 삭제
		// 매물 삭제 시, 해당 매물 조건 및 계약 리스트도 모두 소프트 딜리트 해야함
		property.deleteProperty();
	}

	/**
	 * status 가 반드시 ACTIVE 인 중개사만 조회
	 * @param agentId 중개사 ID
	 * @return 중개사 엔티티
	 * @throws ResourceNotFoundException 해당 중개사를 찾을 수 없는 경우
	 */
	private Agent findAgentById(Long agentId) {
		return agentRepository.findByIdAndStatus(agentId, AgentStatus.ACTIVE)
			.orElseThrow(() -> new ResourceNotFoundException("해당 중개사를 찾을 수 없습니다.", "AGENT_NOT_FOUND"));
	}

	/**
	 * 매물 등록 가능 여부 확인
	 * @param roadAddress 도로명 주소
	 * @param detailAddress 상세 주소
	 * @param customerId 고객 ID
	 */
	public void validateRegisterProperty(String roadAddress, String detailAddress, Long customerId) {
		// 1. 같은 주소 + 다른 고객 + 기존 매물 active = true -> 등록 불가
		List<Property> othersActive = propertyRepository
			.findByRoadAddressAndDetailAddressAndCustomerIdNotAndActive(roadAddress, detailAddress, customerId, true);
		// 2. 같은 주소 + 다른 고객 + 기존 매물 active = false -> 등록 가능
		if (!othersActive.isEmpty()) {
			// 다른 고객이 동일 주소 매물을 등록한 적이 있음
			throw new BusinessException(ErrorCode.DUPLICATE_ACTIVE_PROPERTY_BY_OTHER_CUSTOMER);
		}


		// 3. 같은 주소 + 같은 고객 -> 등록 불가
		Optional<Property> sameCustomerProperty = propertyRepository
			.findByRoadAddressAndDetailAddressAndCustomerId(roadAddress, detailAddress, customerId);
		if (sameCustomerProperty.isPresent()) {
			// 같은 고객이 동일 주소 매물을 등록한 적이 있음
			throw new BusinessException(ErrorCode.DUPLICATE_PROPERTY_BY_SAME_CUSTOMER);
		}
		// 동일 주소, 다른 고객의 활성 매물이 있을 경우 활성화 여부 true로 변경 불가하도록 순서를 이렇게 함
	}

	/**
	 * 해당 매물 id로 존재 여부 확인
	 * @param id 매물 ID
	 * @return 매물 ID로 매물을 찾았을 경우, Property 리턴
	 *         매물을 찾지 못했을 경우, exception 처리
	 */
	public Property findPropertyById(Long id) {
		Property property = propertyRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 매물입니다.", "PROPERTY_NOT_FOUND"));
		return property;
	}

	/**
	 * 해당 고객 id로 존재 여부 확인
	 * @param id 고객 ID
	 * @return 고객 ID로 매물을 찾았을 경우, Customer 리턴
	 *         고객을 찾지 못했을 경우, exception 처리
	 */
	public Customer findCustomerById(Long id) {
		// 의뢰인(임대인 또는 매도인) 존재 여부 확인
		Customer customer = customerRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 고객입니다.", "CUSTOMER_NOT_FOUND"));
		return customer;
	}
}
