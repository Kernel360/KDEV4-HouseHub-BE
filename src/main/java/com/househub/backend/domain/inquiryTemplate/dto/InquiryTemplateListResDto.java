package com.househub.backend.domain.inquiryTemplate.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InquiryTemplateListResDto {
	private List<InquiryTemplateResDto> content;
	private PaginationDto pagination;

	public static InquiryTemplateListResDto fromPage(Page<InquiryTemplateResDto> page) {
		return InquiryTemplateListResDto.builder()
			.content(page.getContent())
			.pagination(PaginationDto.fromPage(page))
			.build();
	}
}

