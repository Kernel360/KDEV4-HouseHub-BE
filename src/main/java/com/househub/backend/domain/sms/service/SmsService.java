package com.househub.backend.domain.sms.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.sms.dto.AligoHistoryResDto;
import com.househub.backend.domain.sms.dto.SendSmsReqDto;
import com.househub.backend.domain.sms.dto.SendSmsResDto;
import com.househub.backend.domain.sms.dto.SmsListResDto;

import jakarta.validation.Valid;

public interface SmsService {

	// 문자 단건 발송
	SendSmsResDto sendSms(SendSmsReqDto sendSmsReqDto, AgentResDto agentDto);

	// 문자 발송 이력 조회
	List<AligoHistoryResDto.HistoryDetailDto> getRecentMessages(Integer page, Integer pageSize, String startDate,
		Integer limitDay);

	SendSmsResDto findById(Long id, AgentResDto agentDto);

	SmsListResDto findAllByKeyword(String keyword, AgentResDto agentDto, Pageable pageable);
}
