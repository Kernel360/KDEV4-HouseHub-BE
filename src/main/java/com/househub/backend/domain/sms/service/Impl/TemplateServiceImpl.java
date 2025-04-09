package com.househub.backend.domain.sms.service.Impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.sms.dto.CreateUpdateTemplateReqDto;
import com.househub.backend.domain.sms.dto.SmsTemplateListResDto;
import com.househub.backend.domain.sms.dto.TemplateResDto;
import com.househub.backend.domain.sms.entity.SmsTemplate;
import com.househub.backend.domain.sms.repository.SmsRepository;
import com.househub.backend.domain.sms.repository.TemplateRepository;
import com.househub.backend.domain.sms.service.TemplateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

	private final TemplateRepository templateRepository;
	private final AgentRepository agentRepository;
	private final SmsRepository smsRepository;

	@Transactional
	@Override
	public TemplateResDto createTemplate(CreateUpdateTemplateReqDto dto, Long agentId) {
		Agent agent = validateAgent(agentId);
		SmsTemplate template = dto.toEntity(agent);

		return templateRepository.save(template).toResDto();
	}

	@Transactional
	@Override
	public TemplateResDto updateTemplate(CreateUpdateTemplateReqDto dto, Long id, Long agentId) {
		Agent agent = validateAgent(agentId);
		SmsTemplate template = validateTemplate(id, agent);

		template.update(dto);

		return templateRepository.save(template).toResDto();
	}

	@Transactional
	@Override
	public SmsTemplate deleteTemplate(Long id, Long agentId) {
		Agent agent = validateAgent(agentId);
		SmsTemplate template = validateTemplate(id, agent);

		return template.delete();
	}

	@Override
	public SmsTemplateListResDto findAll(String keyword, Long agentId, Pageable pageable) {
		Agent agent = validateAgent(agentId);

		Page<SmsTemplate> templatePage = smsRepository.findAllByAgentAndFiltersAndDeletedAtIsNull(
			agent.getId(),
			keyword,
		keyword,
		pageable
		);

		Page<TemplateResDto> response = templatePage.map(TemplateResDto::fromEntity);
		return SmsTemplateListResDto.fromPage(response);
	}

	@Override
	public TemplateResDto findTemplate(Long id, Long agentId) {
		Agent agent = validateAgent(agentId);
		SmsTemplate template = validateTemplate(id, agent);

		return template.toResDto();
	}

	private Agent validateAgent(Long agentId) {
		return agentRepository.findById(agentId)
			.orElseThrow(() -> new ResourceNotFoundException("공인중개사가 존재하지 않습니다.", "AGENT_NOT_FOUND"));
	}

	private SmsTemplate validateTemplate(Long id, Agent agent) {
		return templateRepository.findByIdAndAgentAndDeletedAtIsNull(id, agent)
			.orElseThrow(() -> new ResourceNotFoundException("템플릿이 존재하지 않습니다.", "TEMPLATE_NOT_FOUND"));
	}
}
