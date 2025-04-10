package com.househub.backend.domain.inquiry.entity;

import com.househub.backend.domain.inquiryTemplate.entity.Question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inquiry_answers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class InquiryAnswer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 어떤 문의에 대한 답변인지
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inquiry_id")
	private Inquiry inquiry;

	// 어떤 질문에 대한 답변인지
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id")
	private Question question;

	// answer 는 다양한 타입을 담을 수 있도록 JSON 문자열로 저장
	@Column(nullable = false, columnDefinition = "TEXT")
	private String answer;
}
