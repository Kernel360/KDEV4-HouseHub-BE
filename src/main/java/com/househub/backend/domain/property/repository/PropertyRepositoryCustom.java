package com.househub.backend.domain.property.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.property.dto.PropertySearchDto;
import com.househub.backend.domain.property.entity.Property;

public interface PropertyRepositoryCustom {
	Page<Property> searchProperties(Long agentId, PropertySearchDto searchDto, Pageable pageable);
}

