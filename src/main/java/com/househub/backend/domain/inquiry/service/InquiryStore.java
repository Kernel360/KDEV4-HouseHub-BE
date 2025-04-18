package com.househub.backend.domain.inquiry.service;

import com.househub.backend.domain.inquiry.entity.Inquiry;
import com.househub.backend.domain.inquiry.entity.InquiryAnswer;

public interface InquiryStore {
	Inquiry create(Inquiry inquiry);

	void createAnswer(InquiryAnswer inquiryAnswer);
}
