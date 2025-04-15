package com.househub.backend.domain.customer.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.customer.domain.entity.Customer;

public interface CustomerReader {
	public Customer getCustomerById(Long id, Long agentId);
	public Customer getCustomerByContact(String contact, Long agentId);
	public void checkCustomer(String contact, Long agentId);
	public Page<Customer> getAllCustomer(String keyword, Long agentId, Pageable pageable);
}
