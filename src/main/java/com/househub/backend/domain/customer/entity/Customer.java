package com.househub.backend.domain.customer.entity;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.common.validation.ValidBirthDate;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.consultation.entity.Consultation;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.customer.dto.CustomerReqDto;
import com.househub.backend.domain.customer.enums.CustomerStatus;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers", uniqueConstraints = {
	@UniqueConstraint(name = "UK_contact_agentId", columnNames = {"contact","agent_id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50)
	private String name;

	@ValidBirthDate
	private LocalDate birthDate;

	@Column(nullable = false)
	private String contact;

	private String email;

	@Column(columnDefinition = "TEXT")
	private String memo;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private LocalDateTime deletedAt;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CustomerStatus status = CustomerStatus.POTENTIAL; // 기본값: 잠재 고객

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_id", nullable = false)
	private Agent agent;

	@OneToMany(mappedBy = "customer")
	@Builder.Default
	private List<Consultation> consultations = new ArrayList<>();

	@OneToMany(mappedBy = "customer")
	@Builder.Default
	private List<Contract> contracts = new ArrayList<>();

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
		status = CustomerStatus.POTENTIAL; // 기본값: 잠재 고객
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public void update(CustomerReqDto reqDto) {
		this.name = reqDto.getName();
		this.email = reqDto.getEmail();
		if (reqDto.getContact() != null && !reqDto.getContact().isEmpty()) {
			this.contact = reqDto.getContact();
		}
		if (reqDto.getBirthDate() != null) {
			this.birthDate = reqDto.getBirthDate();
		}
		this.gender = reqDto.getGender(); // null 허용
		this.memo = reqDto.getMemo(); // null 허용
	}

	public void softDelete() {
		this.deletedAt = LocalDateTime.now();
	}

	public void restore() {
		this.deletedAt = null;
	}
}
