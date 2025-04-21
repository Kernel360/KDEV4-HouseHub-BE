package com.househub.backend.domain.sms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.sms.entity.Sms;

public interface SmsReader {
	Page<Sms> findAllByKeyword(Long agentId, String receiver, String msg, Pageable pageable);
	Sms findById(Long id, Long agentId);
}
