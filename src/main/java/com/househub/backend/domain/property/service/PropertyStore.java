package com.househub.backend.domain.property.service;

import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.dto.UpdatePropertyReqDto;
import com.househub.backend.domain.property.entity.Property;

public interface PropertyStore {
	Property create(Property property);

	Property update(UpdatePropertyReqDto reqDto, Property property, Customer customer);

	void delete(Property property);
}

