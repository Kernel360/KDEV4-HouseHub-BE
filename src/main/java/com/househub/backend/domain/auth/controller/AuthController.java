package com.househub.backend.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.common.response.ErrorResponse;
import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.common.util.SecurityUtil;
import com.househub.backend.common.util.SessionManager;
import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.auth.dto.GetSessionResDto;
import com.househub.backend.domain.auth.dto.SendEmailReqDto;
import com.househub.backend.domain.auth.dto.SendEmailResDto;
import com.househub.backend.domain.auth.dto.SignInReqDto;
import com.househub.backend.domain.auth.dto.SignInResDto;
import com.househub.backend.domain.auth.dto.SignUpReqDto;
import com.househub.backend.domain.auth.dto.VerifyEmailReqDto;
import com.househub.backend.domain.auth.exception.EmailVerifiedException;
import com.househub.backend.domain.auth.exception.InvalidPasswordException;
import com.househub.backend.domain.auth.service.AuthService;
import com.househub.backend.domain.notification.service.EmailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final EmailService emailService;

	/**
	 * 회원가입
	 * @param request 회원가입 요청 정보
	 * @return 회원가입 성공 여부
	 */
	@PostMapping("/signup")
	public ResponseEntity<SuccessResponse<Void>> signup(@Valid @RequestBody SignUpReqDto request) {
		log.info("{}", request.getAgent());
		log.info("{}", request.getRealEstate());

		authService.signup(request);

		return ResponseEntity.ok(SuccessResponse.success("회원가입 성공.", "SIGNUP_SUCCESS", null));
	}

	// 로그인
	@PostMapping("/signin")
	public ResponseEntity<SuccessResponse<SignInResDto>> signin(
		@Valid @RequestBody SignInReqDto request
	) {
		log.info("{}: {}", request.getEmail(), request.getPassword());

		log.info("로그인 비즈니스 로직 시작");
		SignInResDto signInAgentInfo = authService.signin(request);
		log.info("로그인 비즈니스 로직 종료");

		// 응답 본문에는 에이전트 정보를 제외하고 상태만 반환
		return ResponseEntity.ok(SuccessResponse.success("로그인 성공.", "SIGNIN_SUCCESS", signInAgentInfo));
	}

	@Operation(summary = "세션 정보 조회", description = "현재 세션에 저장된 사용자 정보를 조회합니다.")
	@ApiResponse(responseCode = "200", description = "세션 정보 조회 성공")
	@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
	@ApiResponse(responseCode = "500", description = "서버 오류")
	@GetMapping("/session")
	public ResponseEntity<SuccessResponse<GetSessionResDto>> getSession(HttpSession session) {
		AgentResDto signInAgent = SecurityUtil.getAuthenticatedAgent();
		GetSessionResDto response = GetSessionResDto.builder()
			.id(signInAgent.getId())
			.name(signInAgent.getName())
			.email(signInAgent.getEmail())
			.role(signInAgent.getRole())
			.isAuthenticated(true)
			.expiresAt(SessionManager.getSessionExpirationTime(session.getMaxInactiveInterval()))
			.build();

		return ResponseEntity.ok(SuccessResponse.success("세션 정보 조회 성공", "GET_SESSION_SUCCESS", response));
	}

	@PostMapping("/email/send")
	public ResponseEntity<SuccessResponse<SendEmailResDto>> sendEmail(@Valid @RequestBody SendEmailReqDto request) {
		// 이미 가입된 사용자인지 확인
		authService.checkEmailAlreadyExists(request.getEmail());
		String authCode = authService.generateAndSaveAuthCode(request.getEmail());
		emailService.sendVerificationCode(request.getEmail(), authCode);
		log.info("인증 코드: {}", authCode);
		return ResponseEntity.ok(SuccessResponse.success("이메일 발송 성공", "EMAIL_SEND_SUCCESS",
			SendEmailResDto.builder().expiresIn(180).build()));
	}

	@PostMapping("/email/verify")
	public ResponseEntity<SuccessResponse<Void>> verifyEmail(@Valid @RequestBody VerifyEmailReqDto request) {
		authService.verifyCode(request.getEmail(), request.getCode());
		return ResponseEntity.ok(SuccessResponse.success("이메일 인증 성공", "EMAIL_VERIFY_SUCCESS", null));
	}

	@ExceptionHandler({
		ResourceNotFoundException.class,
		EmailVerifiedException.class,
		InvalidPasswordException.class
	})
	public ResponseEntity<ErrorResponse> handleLoginExceptions(RuntimeException ex) {
		if (ex instanceof EmailVerifiedException) {
			return ResponseEntity
				.badRequest()
				.body(
					ErrorResponse.builder()
						.message(ex.getMessage())
						.code(((EmailVerifiedException)ex).getCode())
						.build());
		} else if (ex instanceof InvalidPasswordException) {
			return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(
					ErrorResponse.builder()
						.message(ex.getMessage())
						.code(((InvalidPasswordException)ex).getCode())
						.build());
		} else if (ex instanceof ResourceNotFoundException) {
			throw ex;
		} else {
			// 기타 예상치 못한 예외 처리
			return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ErrorResponse.builder().message("로그인 중 알 수 없는 오류가 발생했습니다.").code("LOGIN_ERROR").build());
		}
	}
}
