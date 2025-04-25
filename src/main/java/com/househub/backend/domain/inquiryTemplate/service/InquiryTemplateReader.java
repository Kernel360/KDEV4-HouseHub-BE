package com.househub.backend.domain.inquiryTemplate.service;

import org.springframework.data.domain.Page;

import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplateResDto;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplateSearchCommand;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;

public interface InquiryTemplateReader {
	InquiryTemplate findByToken(String templateToken);

	boolean existsByAgentIdAndName(Long agentId, String name);

	Page<InquiryTemplateResDto> searchInquiryTemplates(InquiryTemplateSearchCommand command);

	InquiryTemplate findInquiryTemplateWithQuestionsByIdAndAgentId(Long templateId, Long agentId);

	InquiryTemplate findInquiryTemplateWithQuestionsByActiveShareToken(String shareToken);

	InquiryTemplate findInquiryTemplateByIdAndAgentId(Long templateId, Long agentId);
}
