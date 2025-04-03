package com.househub.backend.domain.property.service.impl;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.AgentStatus;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.property.dto.*;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.repository.PropertyRepository;
import com.househub.backend.domain.property.service.PropertyService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

	private final PropertyRepository propertyRepository;
	private final CustomerRepository customerRepository;
	private final AgentRepository agentRepository;

	/**
	 * 매물 등록
	 * @param dto 매물 등록 정보 DTO
	 * @param agentId 공인중개사 ID
	 * @return 등록된 매물 id 를 반환하는 DTO
	 */
	@Transactional
	@Override
	public CreatePropertyResDto createProperty(PropertyReqDto dto, Long agentId) {
		// 로그인한 공인중개사 조회
		Agent agent = findAgentById(agentId);
		// 동일한 주소를 가진 매물 있는지 확인
		existsByAddress(dto.getRoadAddress(), dto.getDetailAddress());
		// 의뢰인(임대인 또는 매도인) 존재 여부 확인
		Customer customer = findCustomerById(dto.getCustomerId());
		// dto -> entity
		Property property = dto.toEntity(customer, agent);
		// db에 저장
		propertyRepository.save(property);
		// 응답 객체 리턴
		return new CreatePropertyResDto(property.getId());
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
		// 해당 매물의 계약 리스트도 response 에 포함
		FindPropertyDetailResDto response = FindPropertyDetailResDto.toDto(property);
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
	public List<FindPropertyResDto> findProperties(PropertySearchDto searchDto, Pageable pageable) {
		// 페이지네이션, 검색 필터링 적용하여 매물 조회
		Page<Property> propertyList = propertyRepository.searchProperties(
			searchDto.getProvince(),
			searchDto.getCity(),
			searchDto.getDong(),
			searchDto.getPropertyType(),
			searchDto.getAgentName(),
			searchDto.getCustomerName(),
			pageable
		);
		// 매물 엔티티를 dto 로 변환하여 리스트로 반환
		return propertyList.stream()
			.map(FindPropertyResDto::toDto).toList();
	}

	/**
	 * 매물 정보 수정
	 * @param propertyId 매물 id
	 * @param updateDto 수정된 매물 정보 DTO
	 */
	@Transactional
	@Override
	public void updateProperty(Long propertyId, PropertyReqDto updateDto) {
		// 주소가 동일한 매물이 있는지 확인
		existsByAddress(updateDto.getRoadAddress(), updateDto.getDetailAddress());
		// 의뢰인(임대인 또는 매도인) 존재 여부 확인
		Customer customer = findCustomerById(updateDto.getCustomerId());
		// 매물 조회
		Property property = findPropertyById(propertyId);
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
		property.deleteProperty();
		// 매물 삭제 시, 해당 계약도 모두 소프트 딜리트 해야함
		property.getContracts().forEach(Contract::deleteContract);
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
	 * 전체 주소(도로명 주소 + 상세 주소)로 해당 매물이 이미 존재하는지 확인
	 * @param roadAddress 도로명 주소
	 * @param detailAddress 상세 주소
	 */
	public void existsByAddress(String roadAddress, String detailAddress) {
		boolean isExist = propertyRepository.existsByRoadAddressAndDetailAddress(roadAddress, detailAddress);
		if (isExist) {
			throw new AlreadyExistsException("이미 존재하는 매물 입니다.", "PROPERTY_ALREADY_EXISTS");
		}
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
