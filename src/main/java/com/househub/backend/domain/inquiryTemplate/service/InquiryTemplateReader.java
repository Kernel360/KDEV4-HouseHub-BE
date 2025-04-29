package com.househub.backend.domain.inquiryTemplate.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplateResDto;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplateSearchCommand;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplateSharedToken;

public interface InquiryTemplateReader {
	InquiryTemplate findByToken(String templateToken);

	boolean existsByAgentIdAndName(Long agentId, String name);

	Page<InquiryTemplateResDto> searchInquiryTemplates(InquiryTemplateSearchCommand command);

	InquiryTemplate findInquiryTemplateWithQuestionsByIdAndAgentId(Long templateId, Long agentId);

	InquiryTemplate findInquiryTemplateWithQuestionsByActiveShareToken(String shareToken);

	InquiryTemplate findInquiryTemplateByIdAndAgentId(Long templateId, Long agentId);

	List<InquiryTemplateSharedToken> findAllActiveTokensByTemplateId(Long templateId);
}
