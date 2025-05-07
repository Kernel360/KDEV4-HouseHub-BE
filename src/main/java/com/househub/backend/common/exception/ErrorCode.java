package com.househub.backend.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	CONTRACT_CUSTOMER_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "CONTRACT_CUSTOMER_ALREADY_DELETED", "삭제된 고객이 연결된 계약은 수정할 수 없습니다."),
	CONTRACT_PROPERTY_CUSTOMER_SAME(HttpStatus.BAD_REQUEST, "CONTRACT_PROPERTY_CUSTOMER_SAME", "매물 등록자와 계약 고객이 동일합니다."),

	AUTH_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "AUTH_CODE_MISMATCH", "인증번호가 일치하지 않습니다."),
	AUTH_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "AUTH_CODE_EXPIRED", "인증번호가 만료되었습니다."),

	EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL_SEND_FAILED",
		"이메일 발송 중 오류가 발생했습니다. 이메일 주소를 확인하거나 잠시 후 다시 시도해주세요."),
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "요청한 리소스를 찾을 수 없습니다."),
	EMAIL_MISMATCH(HttpStatus.BAD_REQUEST, "EMAIL_MISMATCH", "해당 이메일과 일치하는 중개사가 존재하지 않습니다."),
	PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "PASSWORD_MISMATCH", "비밀번호가 일치하지 않습니다."),
	INVALID_SHARED_TOKEN(HttpStatus.BAD_REQUEST, "INVALID_SHARED_TOKEN", "유효하지 않은 링크입니다."),
	CONTACT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "CONTACT_ALREADY_EXISTS", "이 전화번호로 등록된 고객이 이미 있어요."),
	INVALID_NOTIFICATION_FILTER(HttpStatus.BAD_REQUEST, "INVALID_NOTIFICATION_FILTER", "유효하지 않은 알림 필터 값입니다."),
	MISSING_CUSTOMER_INFORMATION(HttpStatus.BAD_REQUEST, "MISSING_CUSTOMER_INFORMATION",
		"기존 고객 ID 또는 신규 고객 정보 중 하나는 필수입니다."),
	CONFLICT_CUSTOMER_CONTACT(HttpStatus.CONFLICT,
		"CONFLICT_CUSTOMER_CONTACT", "이미 등록된 연락처입니다. 기존 고객을 찾아 선택하세요."),
	INVALID_INQUIRY_TYPE(HttpStatus.BAD_REQUEST, "INVALID_INQUIRY_TYPE", "유효하지 않은 문의 유형입니다."),
	INVALID_INQUIRY_TEMPLATE_TITLE_MODIFICATION(
		HttpStatus.BAD_REQUEST,
		"INVALID_INQUIRY_TEMPLATE_TITLE_MODIFICATION",
		"기존 템플릿의 제목은 수정할 수 없습니다."
	),
	INVALID_INQUIRY_TEMPLATE_TYPE_MODIFICATION(
		HttpStatus.BAD_REQUEST,
		"INVALID_INQUIRY_TEMPLATE_TYPE_MODIFICATION",
		"기존 템플릿의 유형은 수정할 수 없습니다."
	),
	INVALID_YEAR_MONTH(HttpStatus.BAD_REQUEST, "INVALID_YEAR_MONTH", "yearMonth는 yyyy-MM 형식이어야 합니다.");

	private final HttpStatus httpStatus;
	private final String code;   // 프론트에서 오류 코드 구분용 (예: AUTH_CODE_INVALID)
	private final String message;

	ErrorCode(HttpStatus httpStatus, String code, String message) {
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
	}
}
