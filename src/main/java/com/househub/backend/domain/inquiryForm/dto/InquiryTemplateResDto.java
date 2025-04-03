package com.househub.backend.domain.inquiryForm.dto;

import java.time.LocalDateTime;

import com.househub.backend.domain.inquiryForm.entity.InquiryTemplate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InquiryTemplateResDto {
	private Long id;
	private String name;
	private String description;
	private boolean isActive;
	private LocalDateTime createdAt;

	public static InquiryTemplateResDto fromEntity(InquiryTemplate entity) {
		return InquiryTemplateResDto.builder()
			.id(entity.getId())
			.name(entity.getName())
			.description(entity.getDescription())
			.isActive(entity.getIsActive())
			.createdAt(entity.getCreatedAt())
			.build();
	}
}

