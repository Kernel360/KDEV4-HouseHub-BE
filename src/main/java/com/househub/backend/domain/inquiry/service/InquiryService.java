package com.househub.backend.domain.inquiry.service;

import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.inquiry.dto.CreateInquiryReqDto;
import com.househub.backend.domain.inquiry.dto.CreateInquiryResDto;
import com.househub.backend.domain.inquiry.dto.InquiryDetailResDto;
import com.househub.backend.domain.inquiry.dto.InquiryListResDto;

public interface InquiryService {
	CreateInquiryResDto createInquiry(CreateInquiryReqDto reqDto);

	InquiryListResDto getInquiries(Long agentId, String keyword, Pageable adjustedPageable);

	InquiryDetailResDto getInquiryDetail(Long inquiryId);

	InquiryListResDto findAllByCustomer(Long id, Pageable adjustedPageable, Long agentId);
}
