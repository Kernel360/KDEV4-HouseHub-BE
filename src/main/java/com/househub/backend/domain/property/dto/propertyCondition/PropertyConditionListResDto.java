package com.househub.backend.domain.property.dto.propertyCondition;

import java.util.List;

import org.springframework.data.domain.Page;

import com.househub.backend.common.dto.PaginationDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PropertyConditionListResDto {
	private List<PropertyConditionResDto> content;
	private PaginationDto pagination;

	public static PropertyConditionListResDto fromPage(Page<PropertyConditionResDto> page) {
		return PropertyConditionListResDto.builder()
			.content(page.getContent())
			.pagination(PaginationDto.fromPage(page))
			.build();
	}
}
