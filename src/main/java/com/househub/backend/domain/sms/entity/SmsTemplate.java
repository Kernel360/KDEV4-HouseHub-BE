package com.househub.backend.domain.sms.entity;

import java.time.LocalDateTime;
import java.util.Optional;

import com.househub.backend.domain.agent.entity.RealEstate;
import com.househub.backend.domain.sms.dto.CreateUpdateTemplateReqDto;
import com.househub.backend.domain.sms.dto.TemplateResDto;

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

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "sms_templates")
public class SmsTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String content;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private LocalDateTime deletedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "real_estate_id", nullable = false)
	private RealEstate realEstate;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public void update(CreateUpdateTemplateReqDto dto){
		Optional.ofNullable(dto.getContent()).ifPresent(content -> this.content = content);
		Optional.ofNullable(dto.getTitle()).ifPresent(title -> this.title = title);
	}

	public TemplateResDto toResDto() {
		return TemplateResDto.builder()
			.id(this.getId())
			.title(this.getTitle())
			.content(this.getContent())
			.createdAt(this.createdAt)
			.updatedAt(this.updatedAt)
			.deletedAt(this.deletedAt)
			.build();
	}

	public SmsTemplate delete() {
		this.deletedAt = LocalDateTime.now();
		return this;
	}
}
