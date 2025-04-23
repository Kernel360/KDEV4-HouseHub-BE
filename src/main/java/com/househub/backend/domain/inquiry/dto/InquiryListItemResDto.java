package com.househub.backend.domain.inquiry.dto;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.inquiry.entity.Inquiry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InquiryListItemResDto {
	private Long inquiryId;
	private String name;
	private String email;
	private String contact;
	private String createdAt;
	private String customerStatus;

	public static InquiryListItemResDto from(Inquiry inquiry) {
		Customer customer = inquiry.getCustomer();
		return builder()
			.inquiryId(inquiry.getId())
			.name(Optional.ofNullable(customer.getName()).orElse("미입력"))
			.email(Optional.ofNullable(customer.getEmail()).orElse("미입력"))
			.contact(Optional.ofNullable(customer.getContact()).orElse("미입력"))
			.createdAt(inquiry.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
			.customerStatus(customer.getStatus().getName())
			.build();
	}
}
