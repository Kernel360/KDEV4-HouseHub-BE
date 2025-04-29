package com.househub.backend.domain.notification.enums;

public enum NotificationType {
	INQUIRY_CREATED("문의 등록"),
	CONTRACT_EXPIRING("계약 만료 임박");

	private String name;

	NotificationType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}


