package com.househub.backend.domain.sms.service;

import com.househub.backend.domain.sms.entity.SmsTemplate;

public interface TemplateStore {
	SmsTemplate createSmsTemplate(SmsTemplate template);
}
