package com.househub.backend.domain.inquiry.service;

import java.util.List;

import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.inquiry.dto.CreateInquiryReqDto;
import com.househub.backend.domain.inquiry.entity.Inquiry;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;

public interface InquiryExecutor {
	Inquiry executeInquiryCreation(List<CreateInquiryReqDto.AnswerDto> answers, InquiryTemplate template,
		Customer customer);
}
