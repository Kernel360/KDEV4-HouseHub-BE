package com.househub.backend.domain.inquiryTemplate.exception;

// 공유된 템플릿이 비활성화되어 더 이상 접근할 수 없는 경우
public class InactiveTemplateException extends RuntimeException {
	public InactiveTemplateException(String message) {
		super(message);
	}
}
