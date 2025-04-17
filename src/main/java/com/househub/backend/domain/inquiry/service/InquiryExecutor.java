package com.househub.backend.domain.inquiry.service;

import com.househub.backend.domain.inquiry.dto.CreateInquiryCommand;
import com.househub.backend.domain.inquiry.entity.Inquiry;

public interface InquiryExecutor {
	Inquiry executeInquiryCreation(CreateInquiryCommand command);
}
