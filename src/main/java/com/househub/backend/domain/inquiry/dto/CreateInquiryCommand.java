package com.househub.backend.domain.inquiry.dto;

import java.util.List;

import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;

import lombok.Builder;

@Builder
public record CreateInquiryCommand(
	InquiryTemplate template,
	Customer customer,
	List<CreateInquiryReqDto.AnswerDto> answers
) {
}

