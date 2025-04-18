package com.househub.backend.domain.property.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.property.dto.PropertyUpdateReqDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.entity.PropertyCondition;
import com.househub.backend.domain.property.repository.PropertyConditionRepository;
import com.househub.backend.domain.property.repository.PropertyRepository;
import com.househub.backend.domain.property.service.PropertyStore;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PropertyStoreImpl implements PropertyStore {

	private final PropertyRepository propertyRepository;
	private final PropertyConditionRepository propertyConditionRepository;

	@Override
	public Property create(Property property) {
		return propertyRepository.save(property);
	}

	public PropertyCondition createCondition(PropertyCondition propertyCondition) {
		return propertyConditionRepository.save(propertyCondition);
	}

	@Override
	public void update(PropertyUpdateReqDto propertyUpdateReqDto, Property property) {

	}
}
