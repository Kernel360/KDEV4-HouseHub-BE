package com.househub.backend.domain.contract.enums;

public enum ContractType {
	SALE("매매"), JEONSE("전세"), MONTHLY_RENT("월세");

	private final String koreanName;

	ContractType(String koreanName) {
		this.koreanName = koreanName;
	}

	public String getKoreanName() {
		return koreanName;
	}
}
