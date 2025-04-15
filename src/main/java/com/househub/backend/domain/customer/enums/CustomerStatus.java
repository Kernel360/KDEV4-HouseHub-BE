package com.househub.backend.domain.customer.enums;

public enum CustomerStatus {
	POTENTIAL,  // 잠재 고객 (문의만 남긴 상태)
	ACTIVE,     // 상담 중 / 거래 진행 중
	INACTIVE    // 종료, 철회, 장기 미응답
}

