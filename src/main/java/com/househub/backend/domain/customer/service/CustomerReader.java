package com.househub.backend.domain.customer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import com.househub.backend.domain.customer.entity.Customer;

public interface CustomerReader {
	Customer findByIdAndDeletedAtIsNotNullOrThrow(Long id, Long agentId);
	Customer findByIdOrThrow(Long id, Long agentId);
	Customer findByContactOrThrow(String contact, Long agentId);
	void checkDuplicatedByContact(String contact, Long agentId);
	void checkDuplicatedByEmail(String email, Long agentId);
	Page<Customer> findAllByKeyword(String keyword, Long agentId, Pageable pageable, boolean includeDeleted);
	Page<Customer> findNewCustomers(Long agentId, Pageable pageable);
	Optional<Customer> findByContactAndAgentId(String contact, Long agentId);
	List<Customer> findAllByBirthDate();
}
