package com.househub.backend.domain.sms.service;

import com.househub.backend.domain.sms.dto.CreateUpdateTemplateReqDto;
import com.househub.backend.domain.sms.entity.SmsTemplate;

public interface SmsTemplateStore {
	SmsTemplate create(SmsTemplate template);
	SmsTemplate update(SmsTemplate template, CreateUpdateTemplateReqDto dto);
	SmsTemplate delete(SmsTemplate template);
}
