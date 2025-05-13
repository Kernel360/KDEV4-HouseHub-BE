package com.househub.backend.domain.consultation.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

import com.househub.backend.domain.property.entity.Property;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "consultation_property")
@SQLRestriction("deleted_at IS NULL") // 조회 시 자동으로 소프트 삭제 제외
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ConsultationProperty {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "consultation_id", nullable = false)
	private Consultation consultation;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "property_id", nullable = false)
	private Property property;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	private LocalDateTime deletedAt;

	/**
	 * 중복 저장 방지를 위한 equals/hashCode 구현
	 * 상담 + 매물 조합이 유일하도록
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ConsultationProperty that))
			return false;
		return consultation != null && property != null &&
			consultation.getId().equals(that.getConsultation().getId()) &&
			property.getId().equals(that.getProperty().getId());
	}

	@Override
	public int hashCode() {
		return (consultation.getId() + "_" + property.getId()).hashCode();
	}

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public void softDelete() {
		this.deletedAt = LocalDateTime.now();
	}

	public void setConsultation(Consultation consultation) {
		this.consultation = consultation;
	}
}
