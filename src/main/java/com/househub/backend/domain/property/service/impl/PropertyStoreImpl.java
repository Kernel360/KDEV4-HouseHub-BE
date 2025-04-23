package com.househub.backend.domain.property.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.dto.UpdatePropertyReqDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.repository.PropertyRepository;
import com.househub.backend.domain.property.service.PropertyStore;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PropertyStoreImpl implements PropertyStore {

	private final PropertyRepository propertyRepository;

	@Override
	public Property create(Property property) {
		return propertyRepository.save(property);
	}

	@Override
	public Property update(UpdatePropertyReqDto updateDto, Property property, Customer customer) {
		property.update(updateDto, customer);
		return property;
	}

	@Override
	public void delete(Property property) {
		property.softDelete();
	}
}
