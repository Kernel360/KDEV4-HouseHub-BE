package com.househub.backend.domain.auth.enums;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;

public enum TokenType {

	PASSWORD_RESET("PASSWORD_RESET"),
	EMAIL_VERIFICATION("EMAIL_VERIFICATION");

	private final String type;

	TokenType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static TokenType fromString(String type) {
		for (TokenType tokenType : TokenType.values()) {
			if (tokenType.getType().equalsIgnoreCase(type)) {
				return tokenType;
			}
		}
		throw new BusinessException(ErrorCode.INVALID_TOKEN_TYPE); // 유효하지 않은 타입은 예외 처리
	}
}

