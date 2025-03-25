package com.househub.backend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@Getter
public class EmailAlreadyExistsException extends AlreadyExistsException {
    private static final String CODE = "EMAIL_ALREADY_EXISTS";

    public EmailAlreadyExistsException(String message) {
        super(message, CODE);
    }
}
