package com.househub.backend.domain.sms.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.sms.entity.SmsTemplate;
import com.househub.backend.domain.sms.repository.TemplateRepository;
import com.househub.backend.domain.sms.service.TemplateStore;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TemplateStoreImpl implements TemplateStore {

	private final TemplateRepository templateRepository;

	@Override
	public SmsTemplate createSmsTemplate(SmsTemplate template) {
		return templateRepository.save(template);
	}
}
