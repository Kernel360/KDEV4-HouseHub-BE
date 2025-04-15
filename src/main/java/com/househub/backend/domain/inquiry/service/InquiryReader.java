package com.househub.backend.domain.inquiry.service;

import java.util.Map;

import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.Question;

public interface InquiryReader {
	Map<Long, Question> getQuestionMap(InquiryTemplate inquiryTemplate);
}
