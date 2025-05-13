package com.househub.backend.domain.consultation.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.consultation.dto.UpdateConsultationReqDto;
import com.househub.backend.domain.consultation.enums.ConsultationStatus;
import com.househub.backend.domain.consultation.enums.ConsultationType;
import com.househub.backend.domain.customer.entity.Customer;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "consultations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Consultation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_id", nullable = false)
	private Agent agent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ConsultationType consultationType; // PHONE, VISIT

	@Column(columnDefinition = "TEXT")
	private String content;

	@Column(nullable = false)
	private LocalDateTime consultationDate;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ConsultationStatus status; // RESERVED, COMPLETED, CANCELLED

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	private LocalDateTime deletedAt;

	// 상담에서 어떤 매물들을 보여줬는지 조회
	@OneToMany(mappedBy = "consultation", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<ConsultationProperty> consultationProperties = new ArrayList<>();

	@PrePersist
	public void onCreate() {
		if (this.status == null) {
			this.status = ConsultationStatus.RESERVED;
		}
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	public void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public void softDelete() {
		this.deletedAt = LocalDateTime.now();
	}

	public void update(UpdateConsultationReqDto consultationReqDto) {
		// 상담 상태 변경
		updateStatus(consultationReqDto.getStatus());

		// 상담 수단 및 상담 내용 수정
		if (consultationReqDto.getConsultationType() != null) {
			this.consultationType = consultationReqDto.getConsultationType();
		}

		if (consultationReqDto.getContent() != null) {
			this.content = consultationReqDto.getContent();
		}

		// 상담 상태가 RESERVED일 경우만 상담일 수정 가능
		if (this.status == ConsultationStatus.RESERVED && consultationReqDto.getConsultationDate() != null) {
			this.consultationDate = consultationReqDto.getConsultationDate();
		}
	}

	// 상담 상태 변경 로직을 처리하는 메서드
	private void updateStatus(ConsultationStatus newStatus) {
		if (newStatus == null) {
			return; // 상태 변경이 없으면 종료
		}

		// 상담 상태가 이미 완료되었거나 취소된 경우 상태 변경 불가
		if (this.status == ConsultationStatus.COMPLETED || this.status == ConsultationStatus.CANCELED) {
			throw new IllegalStateException("상담이 완료되거나 취소된 상태에서는 수정할 수 없습니다.");
		}

		// 기존 상태가 '예약됨(RESERVED)'일 경우에만 '완료됨(COMPLETED)' 또는 '취소됨(CANCELED)'으로 상태 변경 가능
		if (this.status == ConsultationStatus.RESERVED) {
			if (newStatus == ConsultationStatus.COMPLETED || newStatus == ConsultationStatus.CANCELED) {
				this.status = newStatus;
			} else {
				throw new IllegalStateException("상담 상태는 예약됨에서만 완료됨 또는 취소됨으로 변경 가능합니다.");
			}
		} else {
			throw new IllegalStateException("상담 상태가 예약되지 않은 경우 상태를 변경할 수 없습니다.");
		}
	}

	public void addConsultationProperty(ConsultationProperty cp) {
		consultationProperties.add(cp);
		cp.setConsultation(this);
	}
}
