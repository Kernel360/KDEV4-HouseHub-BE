package com.househub.backend.domain.customer.service;

import com.househub.backend.domain.customer.entity.Customer;

public interface CustomerStore {
	Customer create(Customer customer);
}
