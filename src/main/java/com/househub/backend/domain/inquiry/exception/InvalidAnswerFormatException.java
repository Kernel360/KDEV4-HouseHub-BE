package com.househub.backend.domain.inquiry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class InvalidAnswerFormatException extends RuntimeException {
	private final String code;

	public InvalidAnswerFormatException() {
		super("답변 형식이 올바르지 않습니다.");
		this.code = "INVALID_ANSWER_FORMAT";
	}
}
