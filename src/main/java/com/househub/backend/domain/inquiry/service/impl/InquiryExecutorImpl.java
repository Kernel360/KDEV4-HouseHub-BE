package com.househub.backend.domain.inquiry.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.inquiry.dto.CreateInquiryReqDto;
import com.househub.backend.domain.inquiry.entity.Inquiry;
import com.househub.backend.domain.inquiry.entity.InquiryAnswer;
import com.househub.backend.domain.inquiry.exception.InvalidAnswerFormatException;
import com.househub.backend.domain.inquiry.service.InquiryExecutor;
import com.househub.backend.domain.inquiry.service.InquiryReader;
import com.househub.backend.domain.inquiry.service.InquiryStore;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.Question;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InquiryExecutorImpl implements InquiryExecutor {
	private final InquiryReader inquiryReader;
	private final InquiryStore inquiryStore;
	private final ObjectMapper objectMapper;

	public Inquiry executeInquiryCreation(List<CreateInquiryReqDto.AnswerDto> answers, InquiryTemplate template,
		Customer customer) {
		// 문의 저장
		Inquiry inquiry = inquiryStore.save(Inquiry.builder()
			.template(template)
			.customer(customer)
			.build());

		// 질문 목록 캐싱
		Map<Long, Question> questionMap = inquiryReader.getQuestionMap(template);

		// 답변 저장
		for (CreateInquiryReqDto.AnswerDto answerDto : answers) {
			Question question = questionMap.get(answerDto.getQuestionId());

			String serializedAnswer = serializeAnswer(answerDto.getAnswerText());

			InquiryAnswer answer = InquiryAnswer.builder()
				.inquiry(inquiry)
				.question(question)
				.answer(serializedAnswer)
				.build();

			inquiryStore.saveAnswer(answer);
		}

		return inquiry;
	}

	private String serializeAnswer(Object answerText) {
		try {
			if (answerText instanceof String) {
				return (String)answerText;
			}
			return objectMapper.writeValueAsString(answerText);
		} catch (JsonProcessingException e) {
			throw new InvalidAnswerFormatException();
		}
	}
}
