package com.househub.backend.domain.inquiryTemplate.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.QuestionType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InquiryTemplateResDto {
	private Long id;
	private String name;
	private String description;
	private List<QuestionDto> questions;
	@JsonProperty("isActive")
	private Boolean active;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static InquiryTemplateResDto fromEntity(InquiryTemplate entity) {
		return InquiryTemplateResDto.builder()
			.id(entity.getId())
			.name(entity.getName())
			.description(entity.getDescription())
			.questions(entity.getQuestions().stream()
				.map(question -> QuestionDto.builder()
					.id(question.getId())
					.label(question.getLabel())
					.type(question.getType())
					.required(question.getRequired())
					.options(question.getOptions())
					.questionOrder(question.getQuestionOrder())
					.build())
				.toList())
			.active(entity.getActive())
			.createdAt(entity.getCreatedAt())
			.updatedAt(entity.getUpdatedAt())
			.build();
	}

	@Getter
	@Builder
	public static class QuestionDto {
		private Long id;

		private String label;

		private QuestionType type;

		@JsonProperty("isRequired")
		private Boolean required;
		private List<String> options;

		private int questionOrder;
	}
}

