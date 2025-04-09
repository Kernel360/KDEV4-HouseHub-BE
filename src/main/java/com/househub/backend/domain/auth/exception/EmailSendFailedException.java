package com.househub.backend.domain.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Getter
public class EmailSendFailedException extends RuntimeException {
	private final String code;

	public EmailSendFailedException() {
		super("이메일 발송 중 오류가 발생했습니다. 이메일 주소를 확인하거나 잠시 후 다시 시도해주세요.");
		this.code = "EMAIL_SEND_FAILED";
	}
}
