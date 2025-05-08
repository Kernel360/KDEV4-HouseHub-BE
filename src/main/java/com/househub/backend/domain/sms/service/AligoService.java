package com.househub.backend.domain.sms.service;

import java.util.List;

import com.househub.backend.domain.sms.dto.AligoHistoryResDto;
import com.househub.backend.domain.sms.dto.AligoSmsResDto;
import com.househub.backend.domain.sms.dto.SendSmsReqDto;

public interface AligoService {
	AligoSmsResDto sendSms(SendSmsReqDto request);

	List<AligoHistoryResDto.HistoryDetailDto> getRecentMessages(Integer page, Integer pageSize, String startDate, Integer limitDay);
}
