package com.househub.backend.domain.consultation.dto;

import com.househub.backend.domain.customer.entity.Customer;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerSummaryInConsultationDto {
	private Long id;
	private String name;
	private String email;
	private String contact;

	public static CustomerSummaryInConsultationDto fromEntity(Customer customer) {
		return CustomerSummaryInConsultationDto.builder()
			.id(customer.getId())
			.name(customer.getName())
			.email(customer.getEmail())
			.contact(customer.getContact())
			.build();
	}
}
