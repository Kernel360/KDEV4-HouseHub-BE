package com.househub.backend.domain.inquiry.service;

import com.househub.backend.domain.inquiry.entity.Inquiry;
import com.househub.backend.domain.inquiry.entity.InquiryAnswer;

public interface InquiryStore {
	Inquiry save(Inquiry inquiry);

	void saveAnswer(InquiryAnswer inquiryAnswer);
}
