package com.househub.backend.domain.customer.enums;

import lombok.Getter;

@Getter
public enum CustomerExcelColumn {
	NAME("name"),
	BIRTH_DATE("birthDate"),
	CONTACT("contact"),
	EMAIL("email"),
	MEMO("memo"),
	GENDER("gender");

	private final String header;

	CustomerExcelColumn(String header) {
		this.header = header;
	}

	public static CustomerExcelColumn fromIndex(int index) {
		return values()[index];
	}

	public static int size() {
		return values().length;
	}
}