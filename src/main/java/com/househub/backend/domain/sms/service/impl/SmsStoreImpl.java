package com.househub.backend.domain.sms.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.sms.entity.Sms;
import com.househub.backend.domain.sms.repository.SmsRepository;
import com.househub.backend.domain.sms.service.SmsStore;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SmsStoreImpl implements SmsStore {

	private final SmsRepository smsRepository;

	@Override
	public Sms create(Sms sms) {
		return smsRepository.save(sms);
	}
}
