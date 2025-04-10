package com.househub.backend.domain.property.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.househub.backend.common.dto.PaginationDto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PropertyListResDto {
	private List<FindPropertyResDto> content;
	private PaginationDto pagination;

	public static PropertyListResDto fromPage(Page<FindPropertyResDto> page) {
		return PropertyListResDto.builder()
			.content(page.getContent())
			.pagination(PaginationDto.fromPage(page))
			.build();
	}
}
