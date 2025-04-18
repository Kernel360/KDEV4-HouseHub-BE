package com.househub.backend.domain.customer.service.Impl;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.service.CustomerExecutor;
import com.househub.backend.domain.customer.service.CustomerReader;
import com.househub.backend.domain.customer.service.CustomerStore;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerExecutorImpl implements CustomerExecutor {

	private final CustomerReader customerReader;
	private final CustomerStore customerStore;

	@Override
	public Customer validateAndUpdate(Long id, CreateCustomerReqDto request, Agent agent) {
		Customer customer = customerReader.findByIdOrThrow(id, agent.getId());
		if (!customer.getContact().equals(request.getContact())) {
			customerReader.checkDuplicatedByContact(request.getContact(), agent.getId());
		}
		return customerStore.update(customer, request);
	}

	@Override
	public Customer validateAndDelete(Long id, Agent agent) {
		Customer customer = customerReader.findByIdOrThrow(id, agent.getId());
		return customerStore.delete(customer);
	}
}
