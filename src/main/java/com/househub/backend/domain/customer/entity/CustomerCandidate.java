package com.househub.backend.domain.customer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 아직 고객으로 확정되지 않은 문의자 정보 임시 저장
@Entity
@Table(name = "customer_candidates")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CustomerCandidate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String contact;

	public static CustomerCandidate create(String name, String email, String phone) {
		return CustomerCandidate.builder()
			.name(name)
			.email(email)
			.contact(phone)
			.build();
	}
}
