package com.househub.backend.domain.inquiryForm.dto;

import java.util.List;

import com.househub.backend.domain.inquiryForm.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryForm.entity.Question;
import com.househub.backend.domain.inquiryForm.entity.QuestionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryTemplatePreviewResDto {
	private Long id;
	private String name;
	private String description;
	private boolean isActive;
	private List<QuestionDto> questions;

	public static InquiryTemplatePreviewResDto fromEntity(InquiryTemplate inquiryTemplate, List<Question> questions) {
		return InquiryTemplatePreviewResDto.builder()
			.id(inquiryTemplate.getId())
			.name(inquiryTemplate.getName())
			.description(inquiryTemplate.getDescription())
			.isActive(inquiryTemplate.isActive())
			.questions(QuestionDto.fromEntities(questions))
			.build();
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class QuestionDto {
		private String label;
		private QuestionType type;
		private boolean required;
		private List<String> options;
		private int questionOrder;

		public static List<QuestionDto> fromEntities(List<Question> questions) {
			return questions.stream()
				.map(question -> QuestionDto.builder()
					.label(question.getLabel())
					.type(question.getType())
					.required(question.isRequired())
					.options(question.getOptions())
					.questionOrder(question.getQuestionOrder())
					.build())
				.toList();
		}
	}
}