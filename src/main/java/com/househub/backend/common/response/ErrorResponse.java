package com.househub.backend.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@Builder
public class ErrorResponse {
    private final boolean success;
    private final String message;
    private final String code;
    private final List<FieldError> errors;

    @Getter
    @Builder
    public static class FieldError {
        private final String field;
        private final String message;
    }
}
