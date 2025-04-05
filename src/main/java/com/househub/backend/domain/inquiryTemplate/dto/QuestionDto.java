package com.househub.backend.domain.inquiryTemplate.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.househub.backend.common.validation.ValidQuestionType;
import com.househub.backend.domain.inquiryTemplate.entity.QuestionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class QuestionDto {
	@NotBlank(message = "질문 레이블은 필수입니다.")
	@Size(max = 255, message = "질문 레이블은 255자를 초과할 수 없습니다.")
	private String label;

	@NotNull(message = "질문 유형은 필수입니다.")
	@ValidQuestionType
	private QuestionType type;

	@JsonProperty("isRequired")
	private Boolean required;

	private List<String> options;

	@NotNull(message = "질문 순서는 필수입니다.")
	private int questionOrder;
}
