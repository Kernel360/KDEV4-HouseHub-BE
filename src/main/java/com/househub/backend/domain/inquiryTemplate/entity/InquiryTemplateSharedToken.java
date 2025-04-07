package com.househub.backend.domain.inquiryTemplate.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inquiry_template_shared_tokens")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryTemplateSharedToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 문의 템플릿 참조
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "template_id", nullable = false)
	private InquiryTemplate template;

	// 공유 토큰 (UUID 등 예측 불가능한 문자열)
	@Column(name = "share_token", unique = true, nullable = false, length = 36)
	private String shareToken;

	// 생성 시간
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	// 만료 시간 (nullable 가능)
	@Column(name = "expired_at")
	private LocalDateTime expiredAt;

	// 공유 활성화 여부
	@Column(nullable = false)
	@Builder.Default
	private Boolean active = true;

	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
	}

	public static InquiryTemplateSharedToken create(InquiryTemplate template) {
		return InquiryTemplateSharedToken.builder()
			.template(template)
			.shareToken(generateShareToken()) // 예측 불가능한 공유 토큰 생성
			.active(template.getActive()) // 템플릿의 상태에 따라 활성화 여부 설정
			.expiredAt(template.getActive() ? LocalDateTime.now().plusMonths(1L) : null) // 활성화된 토큰은 1개월 간 유지
			.build();
	}

	/**
	 * 공유 토큰을 생성하는 메서드
	 * @return UUID 로 생성된 예측 불가능한 문자열
	 */
	private static String generateShareToken() {
		// UUID 로 예측 불가능한 문자열 생성
		return UUID.randomUUID().toString();
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}

