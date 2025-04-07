package com.househub.backend.domain.inquiry.service;

import com.househub.backend.domain.inquiry.dto.CreateInquiryReqDto;
import com.househub.backend.domain.inquiry.dto.CreateInquiryResDto;

public interface InquiryService {
	CreateInquiryResDto createInquiry(CreateInquiryReqDto reqDto);
}
