package com.househub.backend.domain.sms.service;

import java.util.List;

import com.househub.backend.domain.sms.dto.AligoHistoryResDto;
import com.househub.backend.domain.sms.dto.SendSmsReqDto;
import com.househub.backend.domain.sms.dto.SendSmsResDto;

import jakarta.validation.Valid;

public interface SmsService {

	// 문자 단건 발송
	SendSmsResDto sendSms(@Valid SendSmsReqDto sendSmsReqDto,Long id);

	// 문자 발송 이력 조회
	List<AligoHistoryResDto.HistoryDetailDto> getRecentMessages(Integer page, Integer pageSize, String startDate,
		Integer limitDay);

	List<SendSmsResDto> getAll(Long id);

	SendSmsResDto findById(Long id, Long agentId);
}
