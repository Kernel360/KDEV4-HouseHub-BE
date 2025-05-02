package com.househub.backend.domain.customer.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.customer.repository.CustomerTagMapRepository;
import com.househub.backend.domain.customer.service.CustomerTagMapStore;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerTagMapStoreImpl implements CustomerTagMapStore {
	private final CustomerTagMapRepository customerTagMapRepository;

	@Override
	public void deleteByCustomerId(Long customerId) {
		customerTagMapRepository.deleteByCustomerId(customerId);
	}
}
