package com.househub.backend.domain.sms.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.househub.backend.domain.sms.dto.AligoHistoryResDto;
import com.househub.backend.domain.sms.dto.AligoSmsResDto;
import com.househub.backend.domain.sms.dto.SendSmsReqDto;
import com.househub.backend.domain.sms.service.AligoGateway;
import com.househub.backend.domain.sms.service.AligoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AligoServiceImpl implements AligoService {

	private final AligoGateway aligoGateway;

	public AligoSmsResDto sendSms(SendSmsReqDto request) {
		request.setMsgType(request.getMsgType());
		request.setTitle(processTitle(request.getTitle(), request.getMsgType().toString()));

		return aligoGateway.addParamsAndSend(request);
	}

	private String processTitle(String originalTitle, String msgType) {
		return "LMS".equals(msgType)
			? (StringUtils.hasText(originalTitle) ? originalTitle : "제목")
			: null;
	}

	public List<AligoHistoryResDto.HistoryDetailDto> getRecentMessages(Integer page, Integer pageSize,
		String startDate, Integer limitDay) {
		return aligoGateway.getHistory(page, pageSize, startDate, limitDay);
	}
}
