package com.househub.backend.domain.property.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.property.dto.PropertySearchDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.entity.PropertyCondition;

public interface PropertyReader {
	Property findPropertyBy(Long propertyId);

	void validateRegisterProperty(String roadAddress, String detailAddress, Long customerId);

	Page<Property> searchProperties(PropertySearchDto searchDto, Long agentId, Pageable pageable);

	Page<PropertyCondition> findPropertyConditionsByPropertyId(Long propertyId, Pageable pageable);

	PropertyCondition findPropertyConditionBy(Long propertyConditionId);
}
