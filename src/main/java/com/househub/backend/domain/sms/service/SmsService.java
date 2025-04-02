package com.househub.backend.domain.sms.service;

import java.util.List;

import com.househub.backend.domain.sms.dto.SendSmsReqDto;
import com.househub.backend.domain.sms.dto.SmsHistoryResDto;
import com.househub.backend.domain.sms.dto.SmsResDto;

import jakarta.validation.Valid;

public interface SmsService {

	// 문자 단건 발송
	SmsResDto sendSms(@Valid SendSmsReqDto sendSmsReqDto);

	// 문자 발송 이력 조회
	List<SmsHistoryResDto.HistoryDetailDto> getRecentMessages(Integer page, Integer pageSize, String startDate,
		Integer limitDay);

	// 문자 발송 예약
}
