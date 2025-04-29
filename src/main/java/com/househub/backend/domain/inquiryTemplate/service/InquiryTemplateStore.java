package com.househub.backend.domain.inquiryTemplate.service;

import java.util.List;

import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplateSharedToken;
import com.househub.backend.domain.inquiryTemplate.entity.Question;

public interface InquiryTemplateStore {
	InquiryTemplate createTemplate(InquiryTemplate inquiryTemplate);

	InquiryTemplate updateTemplate(InquiryTemplate inquiryTemplate);

	InquiryTemplate deleteTemplate(InquiryTemplate inquiryTemplate);

	void createQuestions(List<Question> questions);

	void createToken(InquiryTemplateSharedToken token);

	void updateToken(InquiryTemplateSharedToken token);
}
