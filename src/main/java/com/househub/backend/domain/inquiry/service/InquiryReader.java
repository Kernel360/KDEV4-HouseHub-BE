package com.househub.backend.domain.inquiry.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.inquiry.entity.Inquiry;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.Question;

public interface InquiryReader {
	Map<Long, Question> getQuestionMap(InquiryTemplate inquiryTemplate);

	Page<Inquiry> findPageByAgentIdAndKeyword(Long agentId, String keyword, Pageable pageable);

	Inquiry findInquiryWithDetailsOrThrow(Long inquiryId);
}
