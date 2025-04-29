package com.househub.backend.domain.customer.enums;

public enum CustomerStatus {
	POTENTIAL("문의 고객"),   // 아직 상담은 진행되지 않은 잠재 고객
	ACTIVE("진행 중"),       // 현재 상담 또는 거래 진행 중인 고객
	INACTIVE("종료됨");      // 거래 종료, 철회, 또는 장기 미응답 고객

	private String name;

	CustomerStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

