package com.househub.backend.common.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.househub.backend.common.response.ErrorResponse;
import com.househub.backend.domain.inquiry.exception.InvalidAnswerFormatException;
import com.househub.backend.domain.inquiry.exception.QuestionNotFoundException;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden // Swagger 문서에서 이 클래스 제외
@RestControllerAdvice
public class GlobalExceptionHandler {
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

	// 유효성 검사 실패 예외 처리
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
		List<ErrorResponse.FieldError> fieldErrors = ex.getFieldErrors().stream()
			.map(fieldError -> ErrorResponse.FieldError.builder()
				.field(fieldError.getField())
				.message(fieldError.getDefaultMessage())
				.build())
			.collect(Collectors.toList());

		ErrorResponse response = ErrorResponse.builder()
			.success(false)
			.message("입력값을 확인해주세요.")
			.code("VALIDATION_ERROR")
			.errors(fieldErrors)
			.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
		ErrorResponse response = ErrorResponse.builder()
			.success(false)
			.message(ex.getMessage())
			.code(ex.getCode())
			.errors(null)
			.build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(NoAssociatedRealEstateException.class)
	public ResponseEntity<ErrorResponse> handleNoAssociatedRealEstateException(NoAssociatedRealEstateException ex) {
		ErrorResponse response = ErrorResponse.builder()
			.success(false)
			.message(ex.getMessage())
			.code("FORBIDDEN")
			.errors(null)
			.build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(InvalidExcelValueException.class)
	public ResponseEntity<ErrorResponse> handleInvalidExcelValueException(InvalidExcelValueException ex) {
		// 필드 오류를 가져옵니다.
		List<ErrorResponse.FieldError> fieldErrors = ex.getFieldErrors();

		// ErrorResponse 객체 생성
		ErrorResponse errorResponse = ErrorResponse.builder()
			.success(false)
			.message(ex.getMessage())
			.code(ex.getCode())
			.errors(fieldErrors)
			.build();

		// HTTP 응답으로 반환
		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(QuestionNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleQuestionNotFound(QuestionNotFoundException e) {
		ErrorResponse response = ErrorResponse.builder()
			.success(false)
			.message(e.getMessage())
			.code(e.getCode())
			.errors(null)
			.build();
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(InvalidAnswerFormatException.class)
	public ResponseEntity<ErrorResponse> handleInvalidAnswerFormat(InvalidAnswerFormatException e) {
		ErrorResponse response = ErrorResponse.builder()
			.success(false)
			.message(e.getMessage())
			.code(e.getCode())
			.errors(null)
			.build();
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
		ErrorCode errorCode = ex.getErrorCode();
		ErrorResponse response = ErrorResponse.builder()
			.success(false)
			.message(errorCode.getMessage())
			.code(errorCode.getCode())
			.errors(null)
			.build();

		return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleInternalServerError(Exception ex) {
		ErrorResponse response = ErrorResponse.builder()
			.success(false)
			.message("서버 내부 오류가 발생했습니다.")
			.code("INTERNAL_SERVER_ERROR")
			.errors(null)
			.build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<ErrorResponse> handleInvalidFormatError(InvalidFormatException e) {
		ErrorResponse response = ErrorResponse.builder()
			.success(false)
			.message(e.getMessage())
			.code(e.getCode())
			.errors(null)
			.build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(SmsSendFailException.class)
	public ResponseEntity<ErrorResponse> handleSmsSendFailError(SmsSendFailException e) {
		ErrorResponse response = ErrorResponse.builder()
			.success(false)
			.message(e.getMessage())
			.code(e.getCode())
			.errors(null)
			.build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}
