package com.househub.backend.domain.inquiry.service.impl;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.inquiry.service.InquiryReader;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.Question;

@Component
public class InquiryReaderImpl implements InquiryReader {
	@Override
	public Map<Long, Question> getQuestionMap(InquiryTemplate inquiryTemplate) {
		return inquiryTemplate.getQuestions().stream()
			.collect(Collectors.toMap(Question::getId, Function.identity()));
	}
}
