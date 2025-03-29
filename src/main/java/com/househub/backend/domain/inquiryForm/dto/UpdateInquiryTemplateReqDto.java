package com.househub.backend.domain.inquiryForm.dto;

import java.util.List;
import java.util.Optional;

import com.househub.backend.common.validation.ValidQuestionType;
import com.househub.backend.domain.inquiryForm.entity.QuestionType;

import jakarta.validation.constraints.Size;
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
public class UpdateInquiryTemplateReqDto {
	@Size(max = 255, message = "템플릿 이름은 255자를 초과할 수 없습니다.")
	private String name;

	@Size(max = 1000, message = "설명은 1000자를 초과할 수 없습니다.")
	private String description;

	private Boolean isActive;

	private List<QuestionDto> questions;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class QuestionDto {
		private Integer questionOrder;

		@Size(max = 255, message = "질문 레이블은 255자를 초과할 수 없습니다.")
		private String label;

		@ValidQuestionType
		private QuestionType type;

		private Boolean required;

		private Optional<List<String>> options;
	}
}
