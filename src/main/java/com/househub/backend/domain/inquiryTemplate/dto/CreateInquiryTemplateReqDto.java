package com.househub.backend.domain.inquiryTemplate.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.househub.backend.domain.inquiryTemplate.enums.InquiryType;

import jakarta.validation.Valid;
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
public class CreateInquiryTemplateReqDto {
	@NotNull(message = "문의 템플릿 유형은 필수입니다.")
	private String type;

	@NotBlank(message = "템플릿 이름은 필수입니다.")
	@Size(max = 255, message = "템플릿 이름은 255자를 초과할 수 없습니다.")
	private String name;

	@Size(max = 1000, message = "설명은 1000자를 초과할 수 없습니다.")
	private String description;

	@Valid
	@NotNull(message = "질문 목록은 필수입니다.")
	@Size(min = 1, message = "질문 목록은 최소 1개 이상이어야 합니다.")
	private List<QuestionDto> questions;

	@JsonProperty("isActive")
	private Boolean active;

	public InquiryType getInquiryType() {
		return InquiryType.fromKorean(this.type);
	}

}