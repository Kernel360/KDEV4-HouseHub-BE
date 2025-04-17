package com.househub.backend.domain.customer.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.customer.service.CustomerReader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerReaderImpl implements CustomerReader {
	private final CustomerRepository customerRepository;

	@Override
	public Optional<Customer> findByContact(String contact) {
		return customerRepository.findByContact(contact);
	}
}
