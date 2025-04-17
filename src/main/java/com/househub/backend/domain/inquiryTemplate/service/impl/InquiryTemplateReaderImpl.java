package com.househub.backend.domain.inquiryTemplate.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplateSharedToken;
import com.househub.backend.domain.inquiryTemplate.repository.InquiryTemplateSharedTokenRepository;
import com.househub.backend.domain.inquiryTemplate.service.InquiryTemplateReader;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InquiryTemplateReaderImpl implements InquiryTemplateReader {
	private final InquiryTemplateSharedTokenRepository sharedTokenRepository;

	@Override
	public InquiryTemplate findByToken(String token) {
		InquiryTemplateSharedToken shareToken = sharedTokenRepository.findByShareTokenAndActiveTrue(token)
			.orElseThrow(() -> new BusinessException(ErrorCode.INVALID_SHARED_TOKEN));

		return shareToken.getTemplate();
	}
}
