package com.househub.backend.domain.inquiryForm.service;

import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.inquiryForm.dto.CreateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryForm.dto.InquiryTemplateListResDto;
import com.househub.backend.domain.inquiryForm.dto.InquiryTemplatePreviewResDto;
import com.househub.backend.domain.inquiryForm.dto.UpdateInquiryTemplateReqDto;

public interface InquiryTemplateService {
	void createNewInquiryTemplate(CreateInquiryTemplateReqDto reqDto);

	InquiryTemplateListResDto getInquiryTemplates(Boolean isActive, Pageable pageable);

	InquiryTemplateListResDto searchInquiryTemplates(String keyword, Pageable pageable);

	InquiryTemplatePreviewResDto previewInquiryTemplate(Long templateId);

	void updateInquiryTemplate(Long templateId, UpdateInquiryTemplateReqDto reqDto);

	void deleteInquiryTemplate(Long templateId);
}
