package com.househub.backend.domain.inquiryForm.dto;

import com.househub.backend.common.validation.ValidQuestionType;
import com.househub.backend.domain.inquiryForm.entity.QuestionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInquiryTemplateReqDto {
    @NotBlank(message = "템플릿 이름은 필수입니다.")
    @Size(max = 255, message = "템플릿 이름은 255자를 초과할 수 없습니다.")
    private String name;

    @Size(max = 1000, message = "설명은 1000자를 초과할 수 없습니다.")
    private String description;

    @Valid
    @NotNull(message = "질문 목록은 필수입니다.")
    @Size(min = 1, message = "질문 목록은 최소 1개 이상이어야 합니다.")
    private List<QuestionDto> questions;

    private boolean isActive;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QuestionDto {
        @NotBlank(message = "질문 레이블은 필수입니다.")
        @Size(max = 255, message = "질문 레이블은 255자를 초과할 수 없습니다.")
        private String label;

        @NotNull(message = "질문 유형은 필수입니다.")
        @ValidQuestionType
        private QuestionType type;

        private boolean required;
        private List<String> options;

        @NotNull(message = "질문 순서는 필수입니다.")
        private int questionOrder;
    }
}
