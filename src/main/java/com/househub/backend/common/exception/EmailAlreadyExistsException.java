package com.househub.backend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@Getter
public class EmailAlreadyExistsException extends AlreadyExistsException {
    public EmailAlreadyExistsException(String message, String code) {
        super(message, code);
    }
}
