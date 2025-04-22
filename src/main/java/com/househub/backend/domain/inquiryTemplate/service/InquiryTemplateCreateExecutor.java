package com.househub.backend.domain.inquiryTemplate.service;

import com.househub.backend.domain.inquiryTemplate.dto.CreateInquiryTemplateReqDto;

public interface InquiryTemplateCreateExecutor {
	void execute(CreateInquiryTemplateReqDto reqDto, Long agentId);
}
