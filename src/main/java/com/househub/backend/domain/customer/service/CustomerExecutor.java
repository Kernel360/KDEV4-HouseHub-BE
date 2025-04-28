package com.househub.backend.domain.customer.service;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.sms.entity.Sms;

public interface CustomerExecutor {
	Customer validateAndUpdate(Long id, CreateCustomerReqDto request, Agent agent);

	Customer validateAndDelete(Long id, Agent agent);

	Customer findOrCreateCustomer(CreateCustomerReqDto request, Agent agent);
}
