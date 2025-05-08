package com.househub.backend.domain.sms.service;

import java.util.List;

import com.househub.backend.domain.sms.dto.AligoHistoryResDto;
import com.househub.backend.domain.sms.dto.AligoSmsResDto;
import com.househub.backend.domain.sms.dto.SendSmsReqDto;

public interface AligoGateway {
	AligoSmsResDto addParamsAndSend(SendSmsReqDto request);
	List<AligoHistoryResDto.HistoryDetailDto> getHistory(Integer page, Integer pageSize, String startDate, Integer limitDay);
}
