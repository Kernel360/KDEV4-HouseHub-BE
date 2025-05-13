package com.househub.backend.domain.sms.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.service.AgentReader;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.service.CustomerReader;
import com.househub.backend.domain.sms.entity.Sms;
import com.househub.backend.domain.sms.service.SmsExecutor;
import com.househub.backend.domain.sms.service.SmsReader;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SmsExecutorImpl implements SmsExecutor {

	private final CustomerReader customerReader;
	private final AgentReader agentReader;
	private final SmsReader smsReader;

	@Override
	public Page<Sms> findAllByCustomer(Long customerId, Pageable pageable, Long agentId) {
		Customer customer = customerReader.findByIdAndDeletedAtIsNotNullOrThrow(customerId, agentId);
		Agent agent = agentReader.findById(agentId);

		String sender = agent.getContact();
		String receiver = customer.getContact();

		return smsReader.findAllBySenderAndReceiver(receiver, sender, pageable);
	}
}
