package com.househub.backend.domain.sms.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.househub.backend.common.dto.PaginationDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SmsListResDto {

	private List<SendSmsResDto> content;
	private PaginationDto pagination;


	public static SmsListResDto fromPage(Page<SendSmsResDto> page) {
		return SmsListResDto.builder()
			.content(page.getContent())
			.pagination(PaginationDto.fromPage(page))
			.build();
	}
}
