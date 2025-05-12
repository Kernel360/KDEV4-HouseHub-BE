package com.househub.backend.domain.region.enums;

public enum RegionLevel {
	DO("도/광역시"),
	SIGUNGU("시/군/구"),
	DONG("읍/면/동"),
	;

	private final String koreanName;

	RegionLevel(String koreanName) {
		this.koreanName = koreanName;
	}

	public String getKoreanName() {
		return koreanName;
	}

	public static RegionLevel fromString(String level) {
		for (RegionLevel regionLevel : RegionLevel.values()) {
			if (regionLevel.name().equalsIgnoreCase(level)) {
				return regionLevel;
			}
		}
		throw new IllegalArgumentException("Invalid region level: " + level);
	}
}
