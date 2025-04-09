package com.househub.backend.domain.inquiry.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class QuestionNotFoundException extends RuntimeException {
	private final String code;

	public QuestionNotFoundException(Long questionId) {
		super("해당 질문을 찾을 수 없습니다. (id: " + questionId + ")");
		this.code = "QUESTION_NOT_FOUND";
	}
}
