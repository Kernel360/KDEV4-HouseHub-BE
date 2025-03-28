package com.househub.backend.domain.inquiryForm.dto;

import com.househub.backend.domain.inquiryForm.entity.InquiryTemplate;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InquiryTemplateResDto {
    private Long id;
    private String name;
    private String description;
    private boolean isActive;
    private LocalDateTime createdAt;

    public static InquiryTemplateResDto fromEntity(InquiryTemplate entity) {
        return InquiryTemplateResDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .isActive(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}

