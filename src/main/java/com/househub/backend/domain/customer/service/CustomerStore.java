package com.househub.backend.domain.customer.service;

import java.util.List;

import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.entity.Customer;

public interface CustomerStore {
	Customer create(Customer customer);

	List<Customer> createAll(List<Customer> customers);

	Customer update(Customer customer, CreateCustomerReqDto request);

	Customer delete(Customer customer);
}
