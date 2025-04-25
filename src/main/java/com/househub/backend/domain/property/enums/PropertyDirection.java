package com.househub.backend.domain.property.enums;

public enum PropertyDirection {
	NORTH("북향"),
	SOUTH("남향"),
	EAST("동향"),
	WEST("서향"),
	NORTHEAST("북동향"),
	NORTHWEST("북서향"),
	SOUTHEAST("남동향"),
	SOUTHWEST("남서향");

	private final String name;

	PropertyDirection(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

