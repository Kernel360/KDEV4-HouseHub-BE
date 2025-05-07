package com.househub.backend.domain.contract.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.entity.Property;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExpiringContractItemResDto {
	private Long id; // 계약 ID
	private String propertyAddress; // 매물 주소
	private String customerName; // 고객 이름
	private String contractType; // 거래 유형 (매매, 전세, 월세)
	private LocalDate expiredAt; // 계약 만료일
	private String displayStatus; // 임박, 만료
	private String dDay;

	public static ExpiringContractItemResDto fromEntity(Contract contract) {
		Customer customer = contract.getCustomer();
		Property property = contract.getProperty();
		return ExpiringContractItemResDto.builder()
			.id(contract.getId())
			.propertyAddress(formatAddress(property))
			.customerName(customer.getName())
			.contractType(contract.getContractType().getKoreanName())
			.expiredAt(contract.getExpiredAt())
			.displayStatus(determineDisplayStatus(contract.getExpiredAt()))
			.dDay(calculateDDay(contract.getExpiredAt()))
			.build();
	}

	private static String formatAddress(Property property) {
		return property.getJibunAddress() + " " + property.getDetailAddress();
	}

	private static String calculateDDay(LocalDate expiredAt) {
		long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), expiredAt);
		if (daysRemaining < 0)
			return "만료됨";
		if (daysRemaining == 0)
			return "D-Day";
		return "D-" + daysRemaining;
	}

	private static String determineDisplayStatus(LocalDate expiredAt) {
		long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), expiredAt);
		if (daysRemaining < 0)
			return "만료";
		if (daysRemaining <= 7)
			return "임박";
		return "정상";
	}
}
