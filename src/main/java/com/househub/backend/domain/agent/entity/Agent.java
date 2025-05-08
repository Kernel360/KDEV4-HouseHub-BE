package com.househub.backend.domain.agent.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.househub.backend.domain.agent.dto.UpdateAgentReqDto;
import com.househub.backend.domain.agent.enums.AgentStatus;
import com.househub.backend.domain.agent.enums.Role;
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
@Table(name = "agents")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 50)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = true, length = 50)
	private String name;

	@Column(nullable = false, unique = true, length = 50)
	private String contact;

	@Column(nullable = true, unique = true, length = 20)
	private String licenseNumber;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AgentStatus status; // PENDING(가입 대기중), ACTIVE(활성화), INACTIVE(비활성화), DELETED(탈퇴), BLOCKED(차단)

	@Column(nullable = true)
	private Long birthdayTemplateId;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column
	private LocalDateTime updatedAt;

	@Column
	private LocalDateTime deletedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "real_estate_id")
	private RealEstate realEstate;

	@OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Customer> customers = new ArrayList<>();

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
		this.status = AgentStatus.ACTIVE;
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public void update(UpdateAgentReqDto request) {
		this.name = request.getName();
		if (request.getEmail() != null && !request.getEmail().isEmpty()) {
			this.email = request.getEmail();
		}
		if (request.getContact() != null && !request.getContact().isEmpty()) {
			this.contact = request.getContact();
		}
		this.licenseNumber = request.getLicenseNumber();
		this.birthdayTemplateId = request.getBirthdayTemplateId();
	}
}
