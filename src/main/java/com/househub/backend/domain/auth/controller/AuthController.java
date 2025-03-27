package com.househub.backend.domain.auth.controller;

import com.househub.backend.common.response.ErrorResponse;
import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.auth.dto.SignInReqDto;
import com.househub.backend.domain.auth.dto.SignInResDto;
import com.househub.backend.domain.auth.dto.SignUpReqDto;
import com.househub.backend.domain.auth.exception.AgentNotFoundException;
import com.househub.backend.domain.auth.exception.EmailVerifiedException;
import com.househub.backend.domain.auth.exception.InvalidPasswordException;
import com.househub.backend.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<Void>> signup(@Valid @RequestBody SignUpReqDto request) {
        log.info("{}", request.getAgent());
        log.info("{}", request.getRealEstate());

        authService.signup(request);

        return ResponseEntity.ok(SuccessResponse.success("회원가입 성공.", "SIGNUP_SUCCESS", null));
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<SuccessResponse<Void>> sigin(
            @Valid @RequestBody SignInReqDto request,
            HttpSession session
    ) {
        log.info("{}: {}", request.getEmail(), request.getPassword());

        SignInResDto signInAgentInfo = authService.signin(request);

        // 세션에 로그인한 중개사 정보 저장
        session.setAttribute("agent", signInAgentInfo);

        // 응답 본문에는 에이전트 정보를제외하고 상태만 반환
        return ResponseEntity.ok(SuccessResponse.success("로그인 성공.", "SIGNIN_SUCCESS", null));
    }

    @ExceptionHandler({
            EmailVerifiedException.class,
            AgentNotFoundException.class,
            InvalidPasswordException.class
    })
    public ResponseEntity<ErrorResponse> handleLoginExceptions(RuntimeException ex) {
        if (ex instanceof EmailVerifiedException) {
            return ResponseEntity
                .badRequest()
                .body(
                    ErrorResponse.builder()
                        .message(ex.getMessage())
                        .code(((EmailVerifiedException) ex).getCode())
                        .build());
        }
        if (ex instanceof AgentNotFoundException) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                    ErrorResponse.builder()
                        .message(ex.getMessage())
                        .code(((AgentNotFoundException) ex).getCode())
                        .build());
        }

        if (ex instanceof InvalidPasswordException) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                        ErrorResponse.builder()
                            .message(ex.getMessage())
                            .code(((InvalidPasswordException) ex).getCode())
                            .build());
        }

        // 기타 예상치 못한 예외 처리
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder().message("로그인 중 알 수 없는 오류가 발생했습니다.").code("LOGIN_ERROR").build());
    }
}
