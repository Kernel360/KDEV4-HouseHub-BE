package com.househub.backend.domain.sms.service;

import com.househub.backend.domain.sms.entity.SmsTemplate;

public interface SmsTemplateStore {
	SmsTemplate create(SmsTemplate template);
}
