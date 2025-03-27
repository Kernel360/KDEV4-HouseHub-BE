package com.househub.backend.domain.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class AgentNotFoundException extends RuntimeException {
    private final String code;

    public AgentNotFoundException(String message, String code) {
        super(message);
        this.code = code;
    }
}
