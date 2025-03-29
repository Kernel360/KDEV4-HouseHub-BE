package com.househub.backend.domain.inquiryForm.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.inquiryForm.dto.CreateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryForm.dto.UpdateInquiryTemplateReqDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "inquiry_templates")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryTemplate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_id", nullable = false)
	private Agent agent;

	@Column(nullable = false, unique = true)
	private String name;

	private String description;

	@OneToMany(mappedBy = "inquiryTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Question> questions = new ArrayList<>();

	private Boolean isActive;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column
	private LocalDateTime updatedAt;

	@Column
	private LocalDateTime deletedAt;

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

	public void setActive(boolean active) {
		this.isActive = active;
	}

	public static InquiryTemplate fromDto(CreateInquiryTemplateReqDto reqDto, Agent agent) {
		return InquiryTemplate.builder()
			.agent(agent)
			.name(reqDto.getName())
			.description(reqDto.getDescription())
			.isActive(reqDto.isActive())
			.createdAt(LocalDateTime.now())
			.build();
	}

	// 각 필드가 null이 아닌 경우에만 업데이트
	public void update(UpdateInquiryTemplateReqDto reqDto) {

		if (reqDto.getName() != null) {
			this.name = reqDto.getName();
		}
		if (reqDto.getDescription() != null) {
			this.description = reqDto.getDescription();
		}
		if (reqDto.getIsActive() != null) {
			this.isActive = reqDto.getIsActive();
		}
		this.updatedAt = LocalDateTime.now();
	}
}
