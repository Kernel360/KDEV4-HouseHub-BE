package com.househub.backend.domain.sms.service;

import com.househub.backend.domain.sms.dto.AligoHistoryResDto;
import com.househub.backend.domain.sms.dto.AligoSmsResDto;
import com.househub.backend.domain.sms.dto.SendSmsReqDto;

public interface AligoService {
	AligoSmsResDto sendSms(SendSmsReqDto request);

	AligoHistoryResDto getRecentMessages(Integer page, Integer pageSize, String startDate, Integer limitDay);
}
