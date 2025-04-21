package com.househub.backend.domain.sms.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.sms.dto.CreateUpdateTemplateReqDto;
import com.househub.backend.domain.sms.entity.SmsTemplate;
import com.househub.backend.domain.sms.service.SmsTemplateExecutor;
import com.househub.backend.domain.sms.service.SmsTemplateReader;
import com.househub.backend.domain.sms.service.SmsTemplateStore;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SmsTemplateExecutorImpl implements SmsTemplateExecutor {

	private final SmsTemplateReader smsTemplateReader;
	private final SmsTemplateStore smsTemplateStore;

	@Override
	public SmsTemplate delete(Long id, Agent agent) {
		SmsTemplate template = smsTemplateReader.findById(id, agent.getId());
		return smsTemplateStore.create(template.delete());
	}

	@Override
	public SmsTemplate update(CreateUpdateTemplateReqDto dto, Long id, Agent agent) {
		SmsTemplate template = smsTemplateReader.findById(id, agent.getId());
		template.update(dto);
		return smsTemplateStore.create(template);
	}
}
