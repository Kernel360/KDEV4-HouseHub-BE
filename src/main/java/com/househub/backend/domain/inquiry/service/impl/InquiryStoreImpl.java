package com.househub.backend.domain.inquiry.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.inquiry.entity.Inquiry;
import com.househub.backend.domain.inquiry.entity.InquiryAnswer;
import com.househub.backend.domain.inquiry.repository.InquiryAnswerRepository;
import com.househub.backend.domain.inquiry.repository.InquiryRepository;
import com.househub.backend.domain.inquiry.service.InquiryStore;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InquiryStoreImpl implements InquiryStore {
	private final InquiryRepository inquiryRepository;
	private final InquiryAnswerRepository answerRepository;

	@Override
	public Inquiry save(Inquiry inquiry) {
		return inquiryRepository.save(inquiry);
	}

	@Override
	public void saveAnswer(InquiryAnswer inquiryAnswer) {
		answerRepository.save(inquiryAnswer);
	}
}
