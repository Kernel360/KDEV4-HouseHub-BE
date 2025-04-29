package com.househub.backend.domain.customer.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.domain.customer.entity.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResDto {
	private Long id;
	private String name;
	private LocalDate birthDate;
	private String contact;
	private String email;
	private String memo;
	private Gender gender;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;

	public static CustomerResDto fromEntity(Customer customer) {
		if (customer.getDeletedAt() != null) {
			return CustomerResDto.builder()
				.id(customer.getId())
				.name("삭제된 고객")
				.contact(customer.getContact())
				.createdAt(customer.getCreatedAt())
				.updatedAt(customer.getUpdatedAt())
				.deletedAt(customer.getDeletedAt())
				.build();
		}
		return CustomerResDto.builder()
			.id(customer.getId())
			.name(customer.getName())
			.birthDate(customer.getBirthDate())
			.contact(customer.getContact())
			.email(customer.getEmail())
			.memo(customer.getMemo())
			.gender(customer.getGender())
			.createdAt(customer.getCreatedAt())
			.updatedAt(customer.getUpdatedAt())
			.deletedAt(customer.getDeletedAt())
			.build();
	}

}
