package com.househub.backend.domain.customer.service;

import java.util.List;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.dto.CustomerReqDto;
import com.househub.backend.domain.customer.entity.Customer;

public interface CustomerExecutor {
	Customer validateAndUpdate(Long id, CustomerReqDto request, Agent agent);

	Customer validateAndDelete(Long id, Agent agent);

	Customer findOrCreateCustomer(CustomerReqDto request, Agent agent);

	Customer validateAndRestore(Long id, Agent agent);

	Customer addTagsToCustomer(Customer customer, List<Long> tagIds);
}
