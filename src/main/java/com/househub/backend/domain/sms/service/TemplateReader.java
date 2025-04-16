package com.househub.backend.domain.sms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.sms.entity.SmsTemplate;

public interface TemplateReader {
	SmsTemplate findTemplateById(Long id, Long agentId);
	Page<SmsTemplate> findAllByKeyword(Long agentId, String title, String content, Pageable pageable);
}
