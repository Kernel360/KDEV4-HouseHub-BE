package com.househub.backend.domain.inquiryForm.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class InquiryTemplateListResDto {
    private List<InquiryTemplateResDto> templates;
    private int totalPages;
    private long totalElements;
    private int size;
    private int currentPage;

    public static InquiryTemplateListResDto fromPage(Page<InquiryTemplateResDto> page) {
        return InquiryTemplateListResDto.builder()
                .templates(page.getContent())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .size(page.getSize())
                .currentPage(page.getNumber())
                .build();
    }
}

