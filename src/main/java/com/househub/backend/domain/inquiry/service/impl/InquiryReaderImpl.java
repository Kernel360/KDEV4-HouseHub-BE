package com.househub.backend.domain.inquiry.service.impl;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
import com.househub.backend.domain.inquiry.entity.Inquiry;
import com.househub.backend.domain.inquiry.repository.InquiryRepository;
import com.househub.backend.domain.inquiry.service.InquiryReader;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.Question;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InquiryReaderImpl implements InquiryReader {
	private final InquiryRepository inquiryRepository;

	@Override
	public Map<Long, Question> getQuestionMap(InquiryTemplate inquiryTemplate) {
		return inquiryTemplate.getQuestions().stream()
			.collect(Collectors.toMap(Question::getId, Function.identity()));
	}

	@Override
	public Page<Inquiry> findPageByAgentIdAndKeyword(Long agentId, String keyword, Pageable pageable) {
		return inquiryRepository.findInquiriesWithKeyword(
			agentId,
			keyword,
			pageable
		);
	}

	@Override
	public Page<Inquiry> findPageByAgentAndCustomerName(Long agentId, String customerName, Pageable pageable) {
		return inquiryRepository.findInquiriesWithCustomer(
			agentId,
			customerName,
			pageable
		);
	}

	@Override
	public Inquiry findInquiryWithDetailsOrThrow(Long inquiryId) {
		return inquiryRepository.findWithDetailsById(inquiryId)
			.orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
	}
}
