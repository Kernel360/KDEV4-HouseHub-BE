package com.househub.backend.domain.inquiryForm.entity;

import com.househub.backend.domain.inquiryForm.dto.CreateInquiryTemplateReqDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inquiry_template_id")
    private InquiryTemplate inquiryTemplate;

    @Column(nullable = false)
    private String label;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    private boolean required;

    @ElementCollection
    @CollectionTable(
            name = "question_options",
            joinColumns = @JoinColumn(name = "question_id")
    )
    @Column(name = "option_value")
    @Builder.Default
    private List<String> options = new ArrayList<>();

    @Column(nullable = false)
    private int questionOrder;

    public static Question fromDto(CreateInquiryTemplateReqDto.QuestionDto questionDto, InquiryTemplate inquiryTemplate) {
        return Question.builder()
                .inquiryTemplate(inquiryTemplate)
                .label(questionDto.getLabel())
                .type(QuestionType.valueOf(questionDto.getType().name()))
                .required(questionDto.isRequired())
                .options(questionDto.getOptions())
                .questionOrder(questionDto.getQuestionOrder())
                .build();
    }
}
