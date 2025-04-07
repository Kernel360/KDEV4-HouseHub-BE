package com.househub.backend.domain.inquiryTemplate.service;

import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.inquiryTemplate.dto.CreateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplateListResDto;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplatePreviewResDto;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplateSharedResDto;
import com.househub.backend.domain.inquiryTemplate.dto.UpdateInquiryTemplateReqDto;

public interface InquiryTemplateService {
	void createNewInquiryTemplate(CreateInquiryTemplateReqDto reqDto, Long agentId);

	InquiryTemplateListResDto getInquiryTemplates(Boolean isActive, String keyword, Pageable pageable, Long agentId);

	InquiryTemplateListResDto searchInquiryTemplates(String keyword, Pageable pageable, Long agentId);

	InquiryTemplatePreviewResDto previewInquiryTemplate(Long templateId, Long agentId);

	void updateInquiryTemplate(Long templateId, UpdateInquiryTemplateReqDto reqDto, Long agentId);

	void deleteInquiryTemplate(Long templateId, Long agentId);

	InquiryTemplateSharedResDto getInquiryTemplateByShareToken(String shareToken);
}
