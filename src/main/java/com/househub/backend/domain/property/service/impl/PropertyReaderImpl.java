package com.househub.backend.domain.property.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.property.dto.PropertySearchDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.repository.PropertyRepository;
import com.househub.backend.domain.property.service.PropertyReader;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PropertyReaderImpl implements PropertyReader {

	private final PropertyRepository propertyRepository;

	@Override
	public Property findByIdOrThrow(Long propertyId, Long agentId) {
		return propertyRepository.findByIdAndAgentId(propertyId, agentId)
			.orElseThrow(() -> new ResourceNotFoundException("해당 ID의 매물을 찾을 수 없습니다.", "PROPERTY_NOT_FOUND"));
	}

	@Override
	public Page<Property> findPageBySearchDto(PropertySearchDto searchDto, Pageable pageable, Long agentId) {
		Page<Property> propertyList = propertyRepository.searchProperties(
			agentId,
			searchDto,
			pageable
		);
		return propertyList;
	}

	@Override
	public void validateUniqueAddressForCustomer(String roadAddress, String detailAddress, Long customerId) {
		boolean isExist = propertyRepository.existsByRoadAddressAndDetailAddressAndCustomerId(roadAddress, detailAddress, customerId);
			if (isExist) {
			throw new AlreadyExistsException("해당 고객이 동일 주소로 등록한 매물이 존재합니다.", "PROPERTY_ALREADY_EXISTS");
		}
	}
}
