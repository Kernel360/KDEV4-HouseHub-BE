package com.househub.backend.domain.inquiryTemplate.entity;

import java.util.ArrayList;
import java.util.List;

import com.househub.backend.domain.inquiryTemplate.dto.QuestionDto;
import com.househub.backend.domain.inquiryTemplate.enums.QuestionType;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	private Boolean required;

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

	public static Question fromDto(QuestionDto questionDto,
		InquiryTemplate inquiryTemplate) {
		return Question.builder()
			.inquiryTemplate(inquiryTemplate)
			.label(questionDto.getLabel())
			.type(QuestionType.valueOf(questionDto.getType().name()))
			.required(questionDto.getRequired())
			.options(questionDto.getOptions())
			.questionOrder(questionDto.getQuestionOrder())
			.build();
	}

	// 각 필드가 null이 아닌 경우에만 업데이트
	public void update(QuestionDto questionDto) {
		// 필수
		this.questionOrder = questionDto.getQuestionOrder();

		if (questionDto.getLabel() != null) {
			this.label = questionDto.getLabel();
		}
		if (questionDto.getType() != null) {
			this.type = questionDto.getType();
		}
		if (questionDto.getRequired() != null) {
			this.required = questionDto.getRequired();
		}
		if (questionDto.getOptions() != null) {
			this.options = questionDto.getOptions();
		}
	}
}
