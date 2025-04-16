package com.househub.backend.domain.sms.service.Impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.sms.entity.SmsTemplate;
import com.househub.backend.domain.sms.repository.TemplateRepository;
import com.househub.backend.domain.sms.service.TemplateReader;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TemplateReaderImpl implements TemplateReader {

	private final TemplateRepository templateRepository;

	@Override
	public SmsTemplate findTemplateById(Long id, Long agentId) {
		return templateRepository.findByIdAndAgentIdAndDeletedAtIsNull(id, agentId)
			.orElseThrow(() -> new ResourceNotFoundException("템플릿이 존재하지 않습니다.", "TEMPLATE_NOT_FOUND"));
	}

	@Override
	public Page<SmsTemplate> findAllByKeyword(Long agentId, String title, String content, Pageable pageable) {
		return templateRepository.findAllByAgentIdAndFiltersAndDeletedAtIsNull(agentId, title, content, pageable);
	}

}
