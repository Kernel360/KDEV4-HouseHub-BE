package com.househub.backend.domain.property.enums;

public enum PropertyType {
	APARTMENT("아파트"),
	VILLA("빌라"),
	OFFICETEL("오피스텔"),
	COMMERCIAL("상가"),
	ONE_ROOM("원룸"),
	TWO_ROOM("투룸");

	private final String koreanName;

	PropertyType(String koreanName) {
		this.koreanName = koreanName;
	}

	public String getKoreanName() {
		return koreanName;
	}

}
