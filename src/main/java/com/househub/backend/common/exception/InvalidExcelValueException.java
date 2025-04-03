package com.househub.backend.common.exception;

import com.househub.backend.common.response.ErrorResponse.FieldError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
@RequiredArgsConstructor
public class InvalidExcelValueException extends RuntimeException {
    private List<FieldError> fieldErrors;
    private final String code;

    public InvalidExcelValueException(String message, List<FieldError> fieldErrors, String code) {
        super(message);
        this.fieldErrors = fieldErrors;
        this.code = code;
    }
}