package com.househub.backend.domain.customer.dto;

import java.time.LocalDate;

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
public class CreateCustomerResDto {
	private Long id;
	private String name;
	private LocalDate birthDate;
	private String contact;
	private String email;
	private String memo;
	private Gender gender;

	public static CreateCustomerResDto fromEntity(Customer customer) {
		return CreateCustomerResDto.builder()
			.id(customer.getId())
			.name(customer.getName())
			.birthDate(customer.getBirthDate())
			.contact(customer.getContact())
			.email(customer.getEmail())
			.memo(customer.getMemo())
			.gender(customer.getGender())
			.build();
	}
}
