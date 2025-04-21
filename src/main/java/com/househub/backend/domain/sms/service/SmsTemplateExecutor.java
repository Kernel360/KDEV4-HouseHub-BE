package com.househub.backend.domain.sms.service;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.sms.dto.CreateUpdateTemplateReqDto;
import com.househub.backend.domain.sms.entity.SmsTemplate;

public interface SmsTemplateExecutor {
	SmsTemplate findAndDelete(Long id, Agent agent);
	SmsTemplate findAndUpdate(CreateUpdateTemplateReqDto dto, Long id, Agent agent);
}
