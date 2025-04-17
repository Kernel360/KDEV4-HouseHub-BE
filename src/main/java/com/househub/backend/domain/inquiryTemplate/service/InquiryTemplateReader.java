package com.househub.backend.domain.inquiryTemplate.service;

import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;

public interface InquiryTemplateReader {
	InquiryTemplate findByToken(String templateToken);
}
