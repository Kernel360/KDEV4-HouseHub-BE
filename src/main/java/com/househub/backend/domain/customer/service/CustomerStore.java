package com.househub.backend.domain.customer.service;

import java.util.List;

import com.househub.backend.domain.tag.entity.Tag;
import com.househub.backend.domain.customer.dto.CustomerReqDto;
import com.househub.backend.domain.customer.entity.Customer;

public interface CustomerStore {
	Customer create(Customer customer);

	List<Customer> createAll(List<Customer> customers);

	Customer update(Customer customer, CustomerReqDto request, List<Tag> tags);

	Customer delete(Customer customer);

	Customer restore(Customer customer);
}
