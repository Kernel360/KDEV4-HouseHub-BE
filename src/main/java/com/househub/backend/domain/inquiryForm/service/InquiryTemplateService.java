package com.househub.backend.domain.inquiryForm.service;

import com.househub.backend.domain.inquiryForm.dto.CreateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryForm.dto.InquiryTemplateListResDto;
import org.springframework.data.domain.Pageable;

public interface InquiryTemplateService {
    void createNewInquiryTemplate(CreateInquiryTemplateReqDto reqDto);

    InquiryTemplateListResDto getInquiryTemplates(Boolean isActive, Pageable pageable);

    InquiryTemplateListResDto searchInquiryTemplates(String keyword, Pageable pageable);
}
