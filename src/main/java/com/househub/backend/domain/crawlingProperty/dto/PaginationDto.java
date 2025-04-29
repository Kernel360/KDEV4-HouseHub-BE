package com.househub.backend.domain.crawlingProperty.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

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
                .currentPage(page.getNumber() + 1)
                .build();
    }
}
