package com.househub.backend.domain.sms.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.sms.dto.AligoHistoryResDto;
import com.househub.backend.domain.sms.dto.AligoSmsResDto;
import com.househub.backend.domain.sms.dto.SendSmsReqDto;
import com.househub.backend.domain.sms.dto.SendSmsResDto;
import com.househub.backend.domain.sms.dto.SmsListResDto;
import com.househub.backend.domain.sms.entity.Sms;
import com.househub.backend.domain.sms.enums.Status;
import com.househub.backend.domain.sms.service.SmsReader;
import com.househub.backend.domain.sms.service.SmsService;
import com.househub.backend.domain.sms.service.SmsStore;
import com.househub.backend.domain.sms.utils.AligoApiClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AligoSmsServiceImpl implements SmsService {

	private final AligoApiClient aligoApiClient;
	private final SmsStore smsStore;
	private final SmsReader smsReader;

	// send와 create 분리
	public SendSmsResDto sendSms(SendSmsReqDto request, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		AligoSmsResDto aligoResponse = aligoApiClient.sendSms(request);
		if(aligoResponse.getResultCode() == 1){
			return SendSmsResDto.fromEntity(smsStore.createSms(request.toEntity(Status.SUCCESS,agent)));
		} else {
			return SendSmsResDto.fromEntity(smsStore.createSms(request.toEntity(Status.FAIL,agent)));
		}
	}
	
	public List<AligoHistoryResDto.HistoryDetailDto> getRecentMessages(Integer page, Integer pageSize, String startDate,
		Integer limitDay) {
		AligoHistoryResDto response = aligoApiClient.getRecentMessages(page,pageSize,startDate,limitDay);
		if (response.getResultCode() == 1) {
			return response.getList();
		} else {
			throw new RuntimeException("전송 내역 조회 실패: " + response.getMessage());
		}
	}

	@Override
	public SmsListResDto findAllByKeyword(String keyword, AgentResDto agentDto, Pageable pageable) {
		Agent agent = agentDto.toEntity();

		Page<Sms> smsPage = smsReader.findAllSmsByKeyword(
			agent.getId(),
			keyword,
			keyword,
			pageable
		);
		Page<SendSmsResDto> response = smsPage.map(SendSmsResDto::fromEntity);
		return SmsListResDto.fromPage(response);
	}

	@Override
	public SendSmsResDto findById(Long id, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		Sms sms = smsReader.findSmsById(id,agent.getId());
		return SendSmsResDto.fromEntity(sms);
	}
}