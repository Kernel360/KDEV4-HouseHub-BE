package com.househub.backend.domain.inquiryForm.service;

import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.inquiryForm.dto.CreateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryForm.dto.InquiryTemplateListResDto;
import com.househub.backend.domain.inquiryForm.dto.InquiryTemplatePreviewResDto;
import com.househub.backend.domain.inquiryForm.dto.UpdateInquiryTemplateReqDto;

public interface InquiryTemplateService {
	void createNewInquiryTemplate(CreateInquiryTemplateReqDto reqDto, Long agentId);

	InquiryTemplateListResDto getInquiryTemplates(Boolean isActive, Pageable pageable, Long agentId);

	InquiryTemplateListResDto searchInquiryTemplates(String keyword, Pageable pageable, Long agentId);

	InquiryTemplatePreviewResDto previewInquiryTemplate(Long templateId, Long agentId);

	void updateInquiryTemplate(Long templateId, UpdateInquiryTemplateReqDto reqDto, Long agentId);

	void deleteInquiryTemplate(Long templateId, Long agentId);
}
