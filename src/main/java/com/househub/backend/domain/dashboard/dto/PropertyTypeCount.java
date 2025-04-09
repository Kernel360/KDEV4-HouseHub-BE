package com.househub.backend.domain.dashboard.dto;

import com.househub.backend.domain.property.enums.PropertyType;

public interface PropertyTypeCount {
	PropertyType getPropertyType();

	Integer getCount();
}
