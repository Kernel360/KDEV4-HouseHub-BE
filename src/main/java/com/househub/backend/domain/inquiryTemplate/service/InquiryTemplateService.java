package com.househub.backend.domain.inquiryTemplate.service;

import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.inquiryTemplate.dto.CreateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplateListResDto;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplatePreviewResDto;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplateSharedResDto;
import com.househub.backend.domain.inquiryTemplate.dto.UpdateInquiryTemplateReqDto;

public interface InquiryTemplateService {
	void createNewInquiryTemplate(CreateInquiryTemplateReqDto reqDto, AgentResDto agent);

	InquiryTemplateListResDto getInquiryTemplates(Boolean isActive, String keyword, String type, Pageable pageable,
		AgentResDto agent);

	InquiryTemplatePreviewResDto previewInquiryTemplate(Long templateId, AgentResDto agent);

	void updateInquiryTemplate(Long templateId, UpdateInquiryTemplateReqDto reqDto, AgentResDto agent);

	void deleteInquiryTemplate(Long templateId, AgentResDto agent);

	InquiryTemplateSharedResDto getInquiryTemplateByShareToken(String shareToken);
}
