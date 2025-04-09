package com.househub.backend.domain.inquiry.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.househub.backend.common.dto.PaginationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InquiryListResDto {
	private List<InquiryListItemResDto> content;
	private PaginationDto pagination;

	public static InquiryListResDto fromPage(Page<InquiryListItemResDto> page) {
		return InquiryListResDto.builder()
			.content(page.getContent())
			.pagination(PaginationDto.fromPage(page))
			.build();
	}
}
