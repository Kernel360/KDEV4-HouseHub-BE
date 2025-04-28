package com.househub.backend.domain.inquiryTemplate.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;

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
	@JsonProperty("isActive")
	private Boolean active;
	private List<InquiryQuestionResDto> questions;

	public static InquiryTemplatePreviewResDto fromEntity(InquiryTemplate inquiryTemplate) {
		return InquiryTemplatePreviewResDto.builder()
			.id(inquiryTemplate.getId())
			.name(inquiryTemplate.getName())
			.description(inquiryTemplate.getDescription())
			.active(inquiryTemplate.getActive())
			.questions(InquiryQuestionResDto.fromEntities(inquiryTemplate.getQuestions()))
			.build();
	}
}