package com.househub.backend.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class SmsSendFailException extends RuntimeException {
	private final String code;

	public SmsSendFailException(String message, String code) {
		super(message);
		this.code = code;
	}
}
