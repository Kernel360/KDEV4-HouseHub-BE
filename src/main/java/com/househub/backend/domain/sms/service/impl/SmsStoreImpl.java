package com.househub.backend.domain.sms.service.impl;

import java.util.List;

import org.springframework.batch.core.configuration.annotation.StepScope;
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

	@StepScope
	@Override
	public void saveAll(List<? extends Sms> logs) {
		smsRepository.saveAll(logs);
	}
}
