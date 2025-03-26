package com.househub.backend.common.exception;

import com.househub.backend.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 유효성 검사 실패 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getFieldErrors().stream()
                .map(fieldError -> ErrorResponse.FieldError.builder().field(fieldError.getField()).message(fieldError.getDefaultMessage()).build())
                .collect(Collectors.toList());

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .message("입력값을 확인해주세요.")
                .code("VALIDATION_ERROR")
                .errors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExistsException(AlreadyExistsException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .code(ex.getCode())
                .errors(null)
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .code(ex.getCode())
                .errors(null)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
