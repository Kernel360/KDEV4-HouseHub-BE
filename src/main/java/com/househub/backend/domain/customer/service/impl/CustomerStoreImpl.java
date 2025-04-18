package com.househub.backend.domain.customer.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.customer.service.CustomerStore;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerStoreImpl implements CustomerStore {
	private final CustomerRepository customerRepository;

	@Transactional
	@Override
	public Customer create(Customer customer) {
		return customerRepository.save(customer);
	}
}
