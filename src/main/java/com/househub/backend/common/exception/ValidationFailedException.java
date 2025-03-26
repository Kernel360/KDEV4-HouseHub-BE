package com.househub.backend.common.exception;

import com.househub.backend.common.response.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class ValidationFailedException extends RuntimeException {
    private final List<ErrorResponse.FieldError> fieldErrors;
    private final String code;

    public ValidationFailedException(String message,
                                     List<ErrorResponse.FieldError> fieldErrors,
                                     String code) {
        super(message);
        this.fieldErrors = fieldErrors;
        this.code = code;
    }
}
