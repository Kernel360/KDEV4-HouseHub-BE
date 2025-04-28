package com.househub.backend.domain.customer.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.consultation.service.ConsultationService;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.service.CustomerExecutor;
import com.househub.backend.domain.customer.service.CustomerReader;
import com.househub.backend.domain.customer.service.CustomerStore;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerExecutorImpl implements CustomerExecutor {
	private final CustomerReader customerReader;
	private final CustomerStore customerStore;
	private final ConsultationService consultationService;

	@Transactional
	@Override
	public Customer findOrCreateCustomer(CreateCustomerReqDto request, Agent agent) {
		// 전화번호로 고객 조회해서 존재하지 않으면 고객 생성
		return customerReader.findByContactAndAgentId(request.getContact(), agent.getId())
			.orElseGet(() -> customerStore.create(
				request.toEntity(agent)
			));
	}

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
