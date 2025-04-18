package com.househub.backend.common.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.househub.backend.common.response.ErrorResponse.FieldError;

import lombok.Getter;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class InvalidExcelValueException extends RuntimeException {
    private final String code;
    private List<FieldError> fieldErrors;

    public InvalidExcelValueException(String message, List<FieldError> fieldErrors, String code) {
        super(message);
        this.fieldErrors = fieldErrors;
        this.code = code;
    }

    public InvalidExcelValueException(String message, String code){
        super(message);
        this.code = code;
    }
}