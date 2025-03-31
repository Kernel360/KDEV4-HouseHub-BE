package com.househub.backend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
@Getter
public class UnauthorizedException extends AuthenticationException {
    private final String code;

    public UnauthorizedException(String message, String code) {
        super(message);
        this.code = code;
    }

    public UnauthorizedException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
