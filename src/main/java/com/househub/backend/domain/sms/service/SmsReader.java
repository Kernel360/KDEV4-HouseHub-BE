package com.househub.backend.domain.sms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.sms.entity.Sms;

public interface SmsReader {
	Page<Sms> findAllSmsByKeyword(Long agentId, String receiver, String msg, Pageable pageable);
	Sms findSmsById(Long id, Long agentId);
}
