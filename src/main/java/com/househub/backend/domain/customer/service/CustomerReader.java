package com.househub.backend.domain.customer.service;

import java.util.Optional;

import com.househub.backend.domain.customer.entity.Customer;

public interface CustomerReader {
	Optional<Customer> findByContact(String contact);
}
