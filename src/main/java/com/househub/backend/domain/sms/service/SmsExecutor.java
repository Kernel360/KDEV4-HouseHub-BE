package com.househub.backend.domain.sms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.sms.entity.Sms;

public interface SmsExecutor {
	Page<Sms> findAllByCustomer(Long customerId, Pageable pageable, Long agentId);
}
