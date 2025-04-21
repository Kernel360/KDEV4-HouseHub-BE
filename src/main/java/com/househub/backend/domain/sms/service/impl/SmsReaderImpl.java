package com.househub.backend.domain.sms.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.househub.backend.domain.sms.entity.Sms;
import com.househub.backend.domain.sms.repository.SmsRepository;
import com.househub.backend.domain.sms.service.SmsReader;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SmsReaderImpl implements SmsReader {

	private final SmsRepository smsRepository;

	@Override
	public Page<Sms> findAllByKeyword(Long agentId, String receiver, String msg, Pageable pageable) {
		receiver = receiver!=null?receiver.replace("-",""):null;
		return smsRepository.findAllSmsByAgentIdAndFiltersAndDeletedAtIsNull(agentId, receiver, msg, pageable);
	}

	@Override
	public Sms findById(Long id, Long agentId) {
		return smsRepository.findByIdAndAgentId(id, agentId);
	}
}
