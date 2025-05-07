package com.househub.backend.domain.property.service;

import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.dto.UpdatePropertyReqDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.tag.entity.Tag;

import java.util.List;

public interface PropertyStore {
	Property create(Property property);

	Property update(UpdatePropertyReqDto reqDto, Property property, Customer customer);

	void addTag(Property property, List<Tag> tags);

	void delete(Property property);
}

