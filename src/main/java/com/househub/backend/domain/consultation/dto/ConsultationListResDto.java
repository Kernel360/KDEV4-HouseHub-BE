package com.househub.backend.domain.consultation.dto;

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
public class ConsultationListResDto {
	private List<ConsultationResDto> content;
	private PaginationDto pagination;

	public static ConsultationListResDto fromPage(Page<ConsultationResDto> page) {
		return ConsultationListResDto.builder()
			.content(page.getContent())
			.pagination(PaginationDto.fromPage(page))
			.build();
	}
}