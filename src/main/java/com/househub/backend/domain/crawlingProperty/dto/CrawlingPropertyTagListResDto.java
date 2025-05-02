package com.househub.backend.domain.crawlingProperty.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CrawlingPropertyTagListResDto {

    private List<CrawlingPropertyTagResDto> content;
    private PaginationDto pagination;

    public static CrawlingPropertyTagListResDto fromPage(Page<CrawlingPropertyTagResDto> page) {
        return CrawlingPropertyTagListResDto.builder()
                .content(page.getContent())
                .pagination(PaginationDto.fromPage(page))
                .build();
    }
}