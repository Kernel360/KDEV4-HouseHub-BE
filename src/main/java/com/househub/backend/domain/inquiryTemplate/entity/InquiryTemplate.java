package com.househub.backend.domain.inquiryTemplate.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.househub.backend.domain.agent.entity.RealEstate;
import com.househub.backend.domain.inquiryTemplate.dto.CreateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryTemplate.dto.UpdateInquiryTemplateReqDto;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "inquiry_templates",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"real_estate_id", "name"})
	}
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryTemplate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "real_estate_id", nullable = false)
	private RealEstate realEstate;

	@Column(nullable = false)
	private String name;

	private String description;

	@OneToMany(mappedBy = "inquiryTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Question> questions = new ArrayList<>();

	private Boolean active;

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
		this.active = active;
	}

	public static InquiryTemplate fromDto(CreateInquiryTemplateReqDto reqDto, RealEstate realEstate) {
		return InquiryTemplate.builder()
			.realEstate(realEstate)
			.name(reqDto.getName())
			.description(reqDto.getDescription())
			.active(reqDto.getActive())
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
		if (reqDto.getActive() != null) {
			this.active = reqDto.getActive();
		}
		this.updatedAt = LocalDateTime.now();
	}
}
