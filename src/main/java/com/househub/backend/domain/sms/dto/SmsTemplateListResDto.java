package com.househub.backend.domain.sms.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.househub.backend.common.dto.PaginationDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SmsTemplateListResDto {

	private List<TemplateResDto> content;
	private PaginationDto pagination;

	public static SmsTemplateListResDto fromPage(Page<TemplateResDto> page) {
		return SmsTemplateListResDto.builder()
			.content(page.getContent())
			.pagination(PaginationDto.fromPage(page))
			.build();
	}
}
