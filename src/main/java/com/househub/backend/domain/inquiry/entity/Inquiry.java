package com.househub.backend.domain.inquiry.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inquiries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Inquiry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 어떤 템플릿을 기준으로 문의가 작성되었는지
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "template_id")
	private InquiryTemplate template;

	// 기존 고객과 매핑되는 경우
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private Customer customer; // null 허용

	// 문의 항목에 대한 실제 답변 리스트
	@OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<InquiryAnswer> answers = new ArrayList<>();

	private LocalDateTime createdAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
}