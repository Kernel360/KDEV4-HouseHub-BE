package com.househub.backend.domain.customer.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.househub.backend.common.dto.PaginationDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerListResDto {
	private List<CustomerResDto> content;
	private PaginationDto pagination;

	public static CustomerListResDto fromPage(Page<CustomerResDto> page) {
		return CustomerListResDto.builder()
			.content(page.getContent())
			.pagination(PaginationDto.fromPage(page))
			.build();
	}
}
