package com.househub.backend.domain.sms.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.sms.dto.CreateUpdateTemplateReqDto;
import com.househub.backend.domain.sms.dto.SmsTemplateListResDto;
import com.househub.backend.domain.sms.dto.TemplateResDto;
import com.househub.backend.domain.sms.entity.SmsTemplate;
import com.househub.backend.domain.sms.service.SmsTemplateExecutor;
import com.househub.backend.domain.sms.service.SmsTemplateReader;
import com.househub.backend.domain.sms.service.SmsTemplateService;
import com.househub.backend.domain.sms.service.SmsTemplateStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SmsSmsTemplateServiceImpl implements SmsTemplateService {

	private final SmsTemplateReader smsTemplateReader;
	private final SmsTemplateStore smsTemplateStore;
	private final SmsTemplateExecutor smsTemplateExecutor;

	@Transactional
	@Override
	public TemplateResDto create(CreateUpdateTemplateReqDto dto, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		SmsTemplate template = dto.toEntity(agent);
		return TemplateResDto.fromEntity(smsTemplateStore.create(template));
	}

	@Transactional
	@Override
	public TemplateResDto update(CreateUpdateTemplateReqDto dto, Long id, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		return TemplateResDto.fromEntity(smsTemplateExecutor.update(dto,id,agent));
	}

	@Transactional
	@Override
	public SmsTemplate delete(Long id, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		return smsTemplateExecutor.delete(id,agent);
	}

	@Override
	public SmsTemplateListResDto findAll(String keyword, AgentResDto agentDto, Pageable pageable) {
		Agent agent = agentDto.toEntity();

		Page<SmsTemplate> templatePage = smsTemplateReader.findAllByKeyword(
			agent.getId(),
			keyword,
			keyword,
			pageable
		);

		Page<TemplateResDto> response = templatePage.map(TemplateResDto::fromEntity);
		return SmsTemplateListResDto.fromPage(response);
	}

	@Override
	public TemplateResDto findById(Long id, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		SmsTemplate template = smsTemplateReader.findById(id, agent.getId());
		return TemplateResDto.fromEntity(template);
	}
}
