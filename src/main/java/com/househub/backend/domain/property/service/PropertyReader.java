package com.househub.backend.domain.property.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.property.dto.PropertySearchDto;
import com.househub.backend.domain.property.entity.Property;

public interface PropertyReader {
	Property findByIdOrThrow(Long propertyId, Long agentId);

	Page<Property> findPageBySearchDto(PropertySearchDto searchDto, Pageable pageable, Long agentId);

	void validateUniqueAddressForCustomer(String roadAddress, String jibunAddress, Long customerId);

	List<Property> searchPropertiesByCustomer(Long agentId, Long customerId);
}
