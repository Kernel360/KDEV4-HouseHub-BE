package com.househub.backend.domain.inquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InquiryListItemResDto {
	private Long inquiryId;
	private CustomerType customerType;
	private String name;
	private String email;
	private String contact;
	private String createdAt;

	public enum CustomerType {
		CUSTOMER,
		CUSTOMER_CANDIDATE
	}
}
