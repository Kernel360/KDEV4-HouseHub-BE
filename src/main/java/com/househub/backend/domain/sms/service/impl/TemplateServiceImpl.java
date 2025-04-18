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
import com.househub.backend.domain.sms.service.TemplateReader;
import com.househub.backend.domain.sms.service.TemplateService;
import com.househub.backend.domain.sms.service.TemplateStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

	private final TemplateReader templateReader;
	private final TemplateStore templateStore;

	@Transactional
	@Override
	public TemplateResDto createTemplate(CreateUpdateTemplateReqDto dto, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		SmsTemplate template = dto.toEntity(agent);
		return TemplateResDto.fromEntity(templateStore.createSmsTemplate(template));
	}

	@Transactional
	@Override
	public TemplateResDto updateTemplate(CreateUpdateTemplateReqDto dto, Long id, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		SmsTemplate template = templateReader.findTemplateById(id, agent.getId());
		template.update(dto);
		return TemplateResDto.fromEntity(templateStore.createSmsTemplate(template));
	}

	@Transactional
	@Override
	public SmsTemplate deleteTemplate(Long id, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		SmsTemplate template = templateReader.findTemplateById(id, agent.getId());
		return templateStore.createSmsTemplate(template.delete());
	}

	@Override
	public SmsTemplateListResDto findAll(String keyword, AgentResDto agentDto, Pageable pageable) {
		Agent agent = agentDto.toEntity();

		Page<SmsTemplate> templatePage = templateReader.findAllByKeyword(
			agent.getId(),
			keyword,
			keyword,
			pageable
		);

		Page<TemplateResDto> response = templatePage.map(TemplateResDto::fromEntity);
		return SmsTemplateListResDto.fromPage(response);
	}

	@Override
	public TemplateResDto findTemplate(Long id, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		SmsTemplate template = templateReader.findTemplateById(id, agent.getId());
		return TemplateResDto.fromEntity(template);
	}
}
