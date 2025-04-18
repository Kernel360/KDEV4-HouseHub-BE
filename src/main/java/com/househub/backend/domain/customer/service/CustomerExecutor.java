package com.househub.backend.domain.customer.service;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.entity.Customer;

public interface CustomerExecutor {
	Customer findOrCreateCustomer(CreateCustomerReqDto request, Agent agent);
}
