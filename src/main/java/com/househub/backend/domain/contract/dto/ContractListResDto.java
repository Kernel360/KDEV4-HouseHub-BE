package com.househub.backend.domain.contract.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.househub.backend.common.dto.PaginationDto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ContractListResDto {
	private List<FindContractResDto> content;
	private PaginationDto pagination;

	public static ContractListResDto fromPage(Page<FindContractResDto> page) {
		return ContractListResDto.builder()
			.content(page.getContent())
			.pagination(PaginationDto.fromPage(page))
			.build();
	}
}
