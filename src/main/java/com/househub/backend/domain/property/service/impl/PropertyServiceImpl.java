package com.househub.backend.domain.property.service.impl;

import java.util.Optional;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.AgentStatus;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.property.dto.*;
import com.househub.backend.domain.property.dto.propertyCondition.PropertyConditionReqDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.entity.PropertyCondition;
import com.househub.backend.domain.property.service.PropertyReader;
import com.househub.backend.domain.property.service.PropertyService;
import com.househub.backend.domain.property.service.PropertyStore;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

	private final PropertyReader propertyReader;
	private final PropertyStore propertyStore;

	private final CustomerRepository customerRepository; // 추후 리팩토링 코드로 변경 예정
	private final AgentRepository agentRepository; // 추후 리팩토링 코드로 변경 예정

	/**
	 * 매물 등록
	 * @param dto 매물 등록 정보 DTO
	 * @param agentId 공인중개사 ID
	 */
	@Transactional
	@Override
	public void createProperty(PropertyReqDto dto, Long agentId) {
		// 1. 로그인한 공인중개사 조회
		Agent agent = findAgentById(agentId);
		// 2. 의뢰인(임대인 또는 매도인) 존재 여부 확인
		Customer customer = findCustomerById(dto.getCustomerId());
		// 3. 매물을 등록할 수 있는 조건인지 확인
		propertyReader.validateRegisterProperty(dto.getRoadAddress(), dto.getDetailAddress(), dto.getCustomerId());
		// 4. dto -> entity
		Property property = dto.toEntity(customer, agent);
		// 5. db에 매물 저장
		propertyStore.create(property);
		// 6. db에 매물 조건 저장
		if(dto.getConditions() != null) {
			for (PropertyConditionReqDto conditionReqDto : dto.getConditions()) {
				PropertyCondition propertyCondition = conditionReqDto.toEntity(property);
				propertyStore.createCondition(propertyCondition);
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
		Property property = propertyReader.findPropertyBy(id);
		// entity -> dto
		return FindPropertyDetailResDto.fromEntity(property);
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
		Page<Property> propertyList = propertyReader.searchProperties(searchDto, agentId, pageable);
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
		Property property = propertyReader.findPropertyBy(propertyId);
		// active false로 변경 시, 매물 조건도 모두 inactive 로 변경 + 주소 유효성 검사 안해도 됨!
		if(updateDto.getActive() != null && updateDto.getActive() == false) {
			property.getConditions().forEach(condition -> condition.updateActiveStatus(false));
			// 주소 또는 고객(의뢰인) 변경 시에만 유효성 검사
		} else if(updateDto.getCustomerId() != null || updateDto.getRoadAddress() != null || updateDto.getDetailAddress() != null) {
			// 주소 관련 필드 미리 계산
			String roadAddress = Optional.ofNullable(updateDto.getRoadAddress()).orElse(property.getRoadAddress());
			String detailAddress = updateDto.getDetailAddress() != null ? updateDto.getDetailAddress() : property.getDetailAddress();
			Long customerId = updateDto.getCustomerId() != null ? updateDto.getCustomerId() : property.getCustomer().getId();
			// 주소 유효성 검사
			propertyReader.validateRegisterProperty(roadAddress, detailAddress, customerId);
 		}
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
		Property property = propertyReader.findPropertyBy(propertyId);
		property.softDelete();
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
