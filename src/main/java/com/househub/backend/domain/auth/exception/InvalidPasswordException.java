package com.househub.backend.domain.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class InvalidPasswordException extends AuthenticationException {
    private final String code;

    public InvalidPasswordException(String message, String code) {
        super(message);
        this.code = code;
    }
}
