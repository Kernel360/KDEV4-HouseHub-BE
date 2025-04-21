package com.househub.backend.domain.sms.service;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.sms.dto.CreateUpdateTemplateReqDto;
import com.househub.backend.domain.sms.entity.SmsTemplate;

public interface SmsTemplateExecutor {
	SmsTemplate delete(Long id, Agent agent);
	SmsTemplate update(CreateUpdateTemplateReqDto dto, Long id, Agent agent);
}
