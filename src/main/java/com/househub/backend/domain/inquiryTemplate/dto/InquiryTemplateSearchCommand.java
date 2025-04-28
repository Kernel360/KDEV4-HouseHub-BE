package com.househub.backend.domain.inquiryTemplate.dto;

import org.springframework.data.domain.Pageable;

public record InquiryTemplateSearchCommand(
	Long agentId,
	Boolean active,
	String keyword,
	String type,
	Pageable pageable
) {
}
