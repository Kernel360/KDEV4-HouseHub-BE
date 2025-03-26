package com.househub.backend.domain.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class EmailVerifiedException extends RuntimeException {
    private final String code;

    public EmailVerifiedException(String message, String code) {
        super(message);
        this.code = code;
    }
}
