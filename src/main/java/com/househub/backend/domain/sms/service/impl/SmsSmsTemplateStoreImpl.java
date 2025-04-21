package com.househub.backend.domain.sms.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.sms.entity.SmsTemplate;
import com.househub.backend.domain.sms.repository.TemplateRepository;
import com.househub.backend.domain.sms.service.SmsTemplateStore;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SmsSmsTemplateStoreImpl implements SmsTemplateStore {

	private final TemplateRepository templateRepository;

	@Override
	public SmsTemplate create(SmsTemplate template) {
		return templateRepository.save(template);
	}
}
