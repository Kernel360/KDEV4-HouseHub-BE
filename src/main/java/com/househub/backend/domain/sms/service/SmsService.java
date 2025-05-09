package com.househub.backend.domain.sms.service;

import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.sms.dto.SendSmsReqDto;
import com.househub.backend.domain.sms.dto.SendSmsResDto;
import com.househub.backend.domain.sms.dto.SmsListResDto;

public interface SmsService {

	// 문자 단건 발송
	SendSmsResDto sendSms(SendSmsReqDto sendSmsReqDto, AgentResDto agentDto);

	SendSmsResDto findById(Long id, AgentResDto agentDto);

	SmsListResDto findAllByKeyword(String keyword, AgentResDto agentDto, Pageable pageable,Long templateId);

	SmsListResDto findAllByCustomer(Long id, Pageable pageable, AgentResDto agentDto);

	float findSmsCostByAgentId(Long agentId);
}
