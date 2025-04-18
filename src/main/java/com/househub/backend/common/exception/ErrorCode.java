package com.househub.backend.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	DUPLICATE_PROPERTY_BY_SAME_CUSTOMER(HttpStatus.BAD_REQUEST, "DUPLICATE_PROPERTY_BY_SAME_CUSTOMER", "이미 동일 고객이 동일 주소로 매물을 등록했습니다."),
	DUPLICATE_ACTIVE_PROPERTY_BY_OTHER_CUSTOMER(HttpStatus.BAD_REQUEST, "DUPLICATE_ACTIVE_PROPERTY_BY_OTHER_CUSTOMER", "해당 주소에 다른 고객의 활성 매물이 존재합니다."),

	CONTRACT_PROPERTY_CUSTOMER_SAME(HttpStatus.BAD_REQUEST, "CONTRACT_PROPERTY_CUSTOMER_SAME", "매물 등록자와 계약 고객이 동일합니다."),
	EXISTING_ACTIVE_CONTRACT_FOR_PROPERTY(HttpStatus.BAD_REQUEST, "EXISTING_ACTIVE_CONTRACT_FOR_PROPERTY", "해당 매물에 대해 진행 중인 계약이 존재합니다."),

	AUTH_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "AUTH_CODE_MISMATCH", "인증번호가 일치하지 않습니다."),
	AUTH_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "AUTH_CODE_EXPIRED", "인증번호가 만료되었습니다."),

	EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL_SEND_FAILED",
		"이메일 발송 중 오류가 발생했습니다. 이메일 주소를 확인하거나 잠시 후 다시 시도해주세요."),
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "요청한 리소스를 찾을 수 없습니다."),
	EMAIL_MISMATCH(HttpStatus.BAD_REQUEST, "EMAIL_MISMATCH", "해당 이메일과 일치하는 중개사가 존재하지 않습니다."),
	PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "PASSWORD_MISMATCH", "비밀번호가 일치하지 않습니다.");

	private final HttpStatus httpStatus;
	private final String code;   // 프론트에서 오류 코드 구분용 (예: AUTH_CODE_INVALID)
	private final String message;

	ErrorCode(HttpStatus httpStatus, String code, String message) {
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
	}
}
