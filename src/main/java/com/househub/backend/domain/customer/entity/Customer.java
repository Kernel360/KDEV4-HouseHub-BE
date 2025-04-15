package com.househub.backend.domain.customer.entity;

import java.time.LocalDateTime;
import java.util.Optional;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = true, length = 50)
	private String name;

	@Column(nullable = true)
	private Integer ageGroup;

	@Column(nullable = false, unique = true)
	private String contact;

	@Column(nullable = true, unique = true)
	private String email;

	@Column(columnDefinition = "TEXT")
	private String memo;

	@Enumerated(EnumType.STRING)
	@Column(nullable = true)
	private Gender gender;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private LocalDateTime deletedAt;

	private CustomerStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_id", nullable = false)
	private Agent agent;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public void update(CreateCustomerReqDto reqDto) {
		Optional.ofNullable(reqDto.getName()).ifPresent(name -> this.name = name);
		Optional.ofNullable(reqDto.getEmail()).ifPresent(email -> this.email = email);
		Optional.ofNullable(reqDto.getContact()).ifPresent(contact -> this.contact = contact);
		this.ageGroup = reqDto.getAgeGroup(); // null 허용
		this.gender = reqDto.getGender(); // null 허용
		this.memo = reqDto.getMemo(); // null 허용
	}

	public void delete() {
		this.deletedAt = LocalDateTime.now();
	}
}
