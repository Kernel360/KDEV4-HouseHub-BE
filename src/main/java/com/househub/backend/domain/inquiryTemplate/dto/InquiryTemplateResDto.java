package com.househub.backend.domain.inquiryTemplate.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InquiryTemplateResDto {
	private Long id;
	private String type;
	private String name;
	private String description;
	private List<InquiryQuestionResDto> questions;
	@JsonProperty("isActive")
	private Boolean active;
	private String shareToken;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static InquiryTemplateResDto fromEntity(InquiryTemplate entity, String shareToken) {
		return InquiryTemplateResDto.builder()
			.id(entity.getId())
			.type(entity.getType().getKoreanName())
			.name(entity.getName())
			.description(entity.getDescription())
			.questions(InquiryQuestionResDto.fromEntities(entity.getQuestions()))
			.active(entity.getActive())
			.shareToken(shareToken)
			.createdAt(entity.getCreatedAt())
			.updatedAt(entity.getUpdatedAt())
			.build();
	}
}

