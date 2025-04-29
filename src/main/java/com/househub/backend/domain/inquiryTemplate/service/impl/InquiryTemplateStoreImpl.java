package com.househub.backend.domain.inquiryTemplate.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplateSharedToken;
import com.househub.backend.domain.inquiryTemplate.entity.Question;
import com.househub.backend.domain.inquiryTemplate.repository.InquiryTemplateRepository;
import com.househub.backend.domain.inquiryTemplate.repository.InquiryTemplateSharedTokenRepository;
import com.househub.backend.domain.inquiryTemplate.repository.QuestionRepository;
import com.househub.backend.domain.inquiryTemplate.service.InquiryTemplateStore;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InquiryTemplateStoreImpl implements InquiryTemplateStore {
	private final InquiryTemplateRepository inquiryTemplateRepository;
	private final QuestionRepository questionRepository;
	private final InquiryTemplateSharedTokenRepository tokenRepository;

	@Override
	public InquiryTemplate createTemplate(InquiryTemplate template) {
		return inquiryTemplateRepository.save(template);
	}

	@Override
	public InquiryTemplate updateTemplate(InquiryTemplate inquiryTemplate) {
		return inquiryTemplateRepository.save(inquiryTemplate);
	}

	@Override
	public InquiryTemplate deleteTemplate(InquiryTemplate inquiryTemplate) {
		return inquiryTemplateRepository.save(inquiryTemplate);
	}

	@Override
	public void createQuestions(List<Question> questions) {
		questionRepository.saveAll(questions);
	}

	@Override
	public void createToken(InquiryTemplateSharedToken token) {
		tokenRepository.save(token);
	}

	@Override
	public void updateToken(InquiryTemplateSharedToken token) {
		tokenRepository.save(token);
	}
}
