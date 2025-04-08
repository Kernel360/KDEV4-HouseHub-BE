package com.househub.backend.common.dto;

import org.springframework.data.domain.Page;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaginationDto {
	private Integer totalPages;
	private Long totalElements;
	private Integer size;
	private Integer currentPage;

	public static <T> PaginationDto fromPage(Page<T> page) {
		return PaginationDto.builder()
			.totalPages(page.getTotalPages())
			.totalElements(page.getTotalElements())
			.size(page.getSize())
			.currentPage(page.getNumber() + 1) // Spring Data JPA는 0부터 시작하므로 1을 더해줌
			.build();
	}
}