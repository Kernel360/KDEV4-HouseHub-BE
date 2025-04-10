package com.househub.backend.domain.inquiryTemplate.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.househub.backend.domain.inquiryTemplate.entity.Question;
import com.househub.backend.domain.inquiryTemplate.entity.QuestionType;

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
public class InquiryQuestionResDto {
	private Long id;
	private String label;
	private QuestionType type;

	@JsonProperty("isRequired")
	private Boolean required;

	private List<String> options;
	private int questionOrder;

	public static InquiryQuestionResDto fromEntity(Question question) {
		return InquiryQuestionResDto.builder()
			.id(question.getId())
			.label(question.getLabel())
			.type(question.getType())
			.required(question.getRequired())
			.options(question.getOptions())
			.questionOrder(question.getQuestionOrder())
			.build();
	}

	public static List<InquiryQuestionResDto> fromEntities(List<Question> questions) {
		return questions.stream()
			.map(InquiryQuestionResDto::fromEntity)
			.toList();
	}
}
