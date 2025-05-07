package com.househub.backend.domain.contract.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.househub.backend.common.dto.PaginationDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExpiringContractListResDto {
	private List<ExpiringContractItemResDto> content;
	private PaginationDto pagination;

	public static ExpiringContractListResDto fromPage(Page<ExpiringContractItemResDto> page) {
		return ExpiringContractListResDto.builder()
			.content(page.getContent())
			.pagination(PaginationDto.fromPage(page))
			.build();
	}
}
