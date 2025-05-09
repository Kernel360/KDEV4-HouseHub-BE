package com.househub.backend.domain.property.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

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
	public List<Property> searchPropertiesByCustomer(Long agentId, Long customerId) {
		return propertyRepository.searchPropertiesByCustomer(
			agentId,
			customerId
		);
	}
}
