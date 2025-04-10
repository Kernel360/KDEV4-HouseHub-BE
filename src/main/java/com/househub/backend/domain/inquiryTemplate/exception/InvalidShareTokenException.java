package com.househub.backend.domain.inquiryTemplate.exception;

// 공유 토큰이 존재하지 않거나 유효하지 않은 경우
public class InvalidShareTokenException extends RuntimeException {
	public InvalidShareTokenException(String message) {
		super(message);
	}
}
