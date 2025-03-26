package com.househub.backend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final String code;


    public ResourceNotFoundException(String message, String code) {
        super(message);
        this.code = code;
    }
}