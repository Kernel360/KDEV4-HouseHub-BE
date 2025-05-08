package com.househub.backend.domain.sms.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.househub.backend.common.exception.SmsSendFailException;
import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.sms.dto.AligoHistoryResDto;
import com.househub.backend.domain.sms.dto.AligoSmsResDto;
import com.househub.backend.domain.sms.dto.SendSmsReqDto;
import com.househub.backend.domain.sms.dto.SendSmsResDto;
import com.househub.backend.domain.sms.dto.SmsListResDto;
import com.househub.backend.domain.sms.dto.SmsTypeCountDto;
import com.househub.backend.domain.sms.entity.Sms;
import com.househub.backend.domain.sms.entity.SmsTemplate;
import com.househub.backend.domain.sms.enums.MessageType;
import com.househub.backend.domain.sms.enums.SmsStatus;
import com.househub.backend.domain.sms.service.AligoService;
import com.househub.backend.domain.sms.service.SmsReader;
import com.househub.backend.domain.sms.service.SmsService;
import com.househub.backend.domain.sms.service.SmsStore;
import com.househub.backend.domain.sms.service.SmsTemplateReader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

	private final AligoService aligoService;
	private final SmsStore smsStore;
	private final SmsReader smsReader;
	private final SmsTemplateReader smsTemplateReader;

	// send와 create 분리
	public SendSmsResDto sendSms(SendSmsReqDto request, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		AligoSmsResDto aligoResponse = aligoService.sendSms(request);
		SmsTemplate template = null;
		if(request.getTemplateId() != null)
			template = smsTemplateReader.findById(request.getTemplateId(),agent.getId());
		if(aligoResponse.getResultCode() == 1){
			return SendSmsResDto.fromEntity(smsStore.create(request.toEntity(SmsStatus.SUCCESS,agent,template)));
		} else {
			smsStore.create(request.toEntity(SmsStatus.FAIL,agent,template));
			throw new SmsSendFailException(aligoResponse.getMessage(),"SMS_SEND_FAIL");
		}
	}

	@Override
	public SmsListResDto findAllByKeyword(String keyword, AgentResDto agentDto, Pageable pageable, Long templateId) {
		Agent agent = agentDto.toEntity();

		Page<Sms> smsPage = smsReader.findAllByKeyword(
			agent.getId(),
			keyword,
			keyword,
			templateId,
			pageable
		);
		Page<SendSmsResDto> response = smsPage.map(SendSmsResDto::fromEntity);
		return SmsListResDto.fromPage(response);
	}

	@Override
	public float findSmsCostByAgentId(Long agentId) {
		List<SmsTypeCountDto> smsTypeCountDtos = smsReader.countSmsByMessageType(agentId);
		float totalCost = 0;
		for (SmsTypeCountDto smsTypeCountDto : smsTypeCountDtos) {
			MessageType type = smsTypeCountDto.getMessageType();
			Long count = smsTypeCountDto.getCount();
			float multiple = type.equals(MessageType.SMS)? 8.4F :type.equals(MessageType.LMS)?25.9F:60F;
			totalCost += (count * multiple);
		}
		// BigDecimal로 반올림 (소수점 2자리, HALF_UP)
		BigDecimal rounded = BigDecimal.valueOf(totalCost).setScale(2, RoundingMode.HALF_UP);
		return rounded.floatValue();
	}

	@Override
	public SendSmsResDto findById(Long id, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		Sms sms = smsReader.findById(id,agent.getId());
		return SendSmsResDto.fromEntity(sms);
	}
}