package com.househub.backend.domain.inquiryTemplate.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.Question;

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
public class InquiryTemplateSharedResDto {
	private String type;
	private String name;
	private String description;
	@JsonProperty("isActive")
	private Boolean active;
	private List<InquiryQuestionResDto> questions;

	public static InquiryTemplateSharedResDto fromEntity(InquiryTemplate inquiryTemplate, List<Question> questions) {
		return InquiryTemplateSharedResDto.builder()
			.type(inquiryTemplate.getType().getKoreanName())
			.name(inquiryTemplate.getName())
			.description(inquiryTemplate.getDescription())
			.active(inquiryTemplate.getActive())
			.questions(InquiryQuestionResDto.fromEntities(questions))
			.build();
	}
}
