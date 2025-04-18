package com.househub.backend.domain.property.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.property.dto.PropertySearchDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.entity.PropertyCondition;
import com.househub.backend.domain.property.repository.PropertyConditionRepository;
import com.househub.backend.domain.property.repository.PropertyRepository;
import com.househub.backend.domain.property.service.PropertyReader;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PropertyReaderImpl implements PropertyReader {

	private final PropertyRepository propertyRepository;
	private final PropertyConditionRepository propertyConditionRepository;

	/**
	 * 해당 매물 id로 존재 여부 확인
	 * @param propertyId 매물 ID
	 * @return 매물 ID로 매물을 찾았을 경우, Property 리턴
	 *         매물을 찾지 못했을 경우, exception 처리
	 */
	@Override
	public Property findPropertyBy(Long propertyId) {
		Property property = propertyRepository.findById(propertyId)
			.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 매물입니다.", "PROPERTY_NOT_FOUND"));
		return property;
	}

	/**
	 * 매물 등록 가능 여부 확인
	 * @param roadAddress 도로명 주소
	 * @param detailAddress 상세 주소
	 * @param customerId 고객 ID
	 */
	@Override
	public void validateRegisterProperty(String roadAddress, String detailAddress, Long customerId) {
		// 1. 같은 주소 + 다른 고객 + 기존 매물 active = true -> 등록 불가
		List<Property> othersActive = propertyRepository
			.findByRoadAddressAndDetailAddressAndCustomerIdNotAndActive(roadAddress, detailAddress, customerId, true);
		if (!othersActive.isEmpty()) {
			// 다른 고객이 동일 주소 매물을 등록한 적이 있음
			throw new BusinessException(ErrorCode.DUPLICATE_ACTIVE_PROPERTY_BY_OTHER_CUSTOMER);
		}
		// 2. 같은 주소 + 같은 고객 -> 등록 불가
		Optional<Property> sameCustomerProperty = propertyRepository
			.findByRoadAddressAndDetailAddressAndCustomerId(roadAddress, detailAddress, customerId);
		if (sameCustomerProperty.isPresent()) {
			// 같은 고객이 동일 주소 매물을 등록한 적이 있음
			throw new BusinessException(ErrorCode.DUPLICATE_PROPERTY_BY_SAME_CUSTOMER);
		}
		// 동일 주소, 다른 고객의 활성 매물이 있을 경우 활성화 여부 true로 변경 불가하도록 순서를 이렇게 함
	}

	@Override
	public Page<Property> searchProperties(PropertySearchDto searchDto, Long agentId, Pageable pageable) {
		return propertyRepository.searchProperties(
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
	}

	@Override
	public Page<PropertyCondition> findPropertyConditionsByPropertyId(Long propertyId, Pageable pageable) {
		return propertyConditionRepository.findAllByPropertyId(propertyId, pageable);
	}

	@Override
	public PropertyCondition findPropertyConditionBy(Long propertyConditionId) {
		return propertyConditionRepository.findById(propertyConditionId)
			.orElseThrow(() -> new ResourceNotFoundException("해당 매물 조건을 찾을 수 없습니다.", "PROPERTY_CONDITION_NOT_FOUND"));
	}
}
