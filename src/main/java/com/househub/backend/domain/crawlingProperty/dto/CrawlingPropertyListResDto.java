package com.househub.backend.domain.crawlingProperty.dto;

import com.househub.backend.domain.crawlingProperty.enums.Direction;
import com.househub.backend.domain.crawlingProperty.enums.PropertyType;
import com.househub.backend.domain.crawlingProperty.enums.TransactionType;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CrawlingPropertyListResDto {

    private List<CrawlingPropertyResDto> content;
    private PaginationDto pagination;

    public static CrawlingPropertyListResDto fromPage(Page<CrawlingPropertyResDto> page) {
        return CrawlingPropertyListResDto.builder()
            .content(page.getContent())
            .pagination(PaginationDto.fromPage(page))
            .build();
    }
}
