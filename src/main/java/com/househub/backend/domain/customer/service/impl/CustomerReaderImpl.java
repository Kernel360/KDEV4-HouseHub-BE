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

	// 고객을 연락처와 agentId 로 조회
	@Override
	public Optional<Customer> findByContactAndAgentId(String contact, Long agentId) {
		return customerRepository.findByContactAndAgentId(contact, agentId);
	}
}
