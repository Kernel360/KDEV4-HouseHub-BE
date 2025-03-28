package com.househub.backend.domain.inquiryForm.service;

import com.househub.backend.domain.inquiryForm.dto.CreateInquiryTemplateReqDto;

public interface InquiryTemplateService {
    void createNewInquiryTemplate(CreateInquiryTemplateReqDto reqDto);
}
