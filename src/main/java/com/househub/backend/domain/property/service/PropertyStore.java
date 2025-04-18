package com.househub.backend.domain.property.service;

import com.househub.backend.domain.property.dto.PropertyUpdateReqDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.entity.PropertyCondition;

public interface PropertyStore {
	Property create(Property property);

	PropertyCondition createCondition(PropertyCondition propertyCondition);

	void update(PropertyUpdateReqDto propertyUpdateReqDto, Property property);
}
