package com.househub.backend.domain.agent.enums;

public enum Role {
	AGENT("ROLE_AGENT"),
	ADMIN("ROLE_ADMIN");

	private final String value;

	Role(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
