package com.househub.backend.domain.inquiryTemplate.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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

	@JsonProperty("isActive")
	private Boolean active;

	private List<QuestionDto> questions;
}
