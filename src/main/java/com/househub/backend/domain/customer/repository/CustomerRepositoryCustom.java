package com.househub.backend.domain.customer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.customer.entity.Customer;

public interface CustomerRepositoryCustom {
	Page<Customer> findAllByAgentIdAndFiltersAndDeletedAtIsNull(
		Long agentId,
		String name,
		String contact,
		String email,
		boolean includeDeleted,
		Pageable pageable);
}
