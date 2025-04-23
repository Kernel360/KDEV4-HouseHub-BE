package com.househub.backend.domain.customer.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.customer.service.CustomerStore;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerStoreImpl implements CustomerStore {

	private final CustomerRepository customerRepository;

	@Override
	public Customer create(Customer customer) {
		return customerRepository.save(customer);
	}

	@Override
	public List<Customer> createAll(List<Customer> customers) {
		return customerRepository.saveAll(customers);
	}

	@Override
	public Customer update(Customer customer, CreateCustomerReqDto request) {
		customer.update(request);
		return customer;
	}

	@Override
	public Customer delete(Customer customer) {
		// 소프트 딜리트
		customer.softDelete();
		return customer;
	}
}

