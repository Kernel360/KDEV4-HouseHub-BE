package com.househub.backend.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.househub.backend.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    /**
     * JSON 직렬화/역직렬화를 위한 ObjectMapper
     */
    private final ObjectMapper objectMapper;

    /**
     * 인증 실패 시 호출되는 메서드 (로그인 전 상태)
     * 다양한 인증 예외에 대해 적절한 에러 응답을 생성하고 전송
     *
     * @param request       현재 HTTP 요청
     * @param response      HTTP 응답 객체
     * @param authException 발생한 인증 예외
     * @throws IOException 응답 작성 중 발생할 수 있는 예외
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 인증 예외에 대한 에러 로깅
        log.error("Authentication Error", authException);

        // 예외 유형에 따른 에러 타입 결정
        AuthenticationErrorType errorType = determineAuthenticationErrorType(authException);

        // 에러 응답 전송
        sendErrorResponse(response, errorType);
    }

    /**
     * 접근 거부 시 호출되는 메서드 (로그인 후 권한 부족 등)
     * 접근 권한이 없는 리소스에 대한 요청 시 처리
     *
     * @param request               현재 HTTP 요청
     * @param response              HTTP 응답 객체
     * @param accessDeniedException 발생한 접근 거부 예외
     * @throws IOException 응답 작성 중 발생할 수 있는 예외
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        // 접근 거부 예외에 대한 에러 로깅
        log.error("Access Denied Error", accessDeniedException);

        // 접근 거부 에러 응답 전송
        sendErrorResponse(response, AuthenticationErrorType.FORBIDDEN);
    }

    /**
     * 발생한 인증 예외의 유형을 판단하여 적절한 에러 타입 결정
     *
     * @param authException 인증 중 발생한 예외
     * @return 결정된 인증 에러 타입
     */
    private AuthenticationErrorType determineAuthenticationErrorType(AuthenticationException authException) {
        // 예외 유형에 따른 에러 타입 매핑
        if (authException instanceof UsernameNotFoundException) {
            return AuthenticationErrorType.AGENT_NOT_FOUND;
        } else if (authException instanceof BadCredentialsException) {
            return AuthenticationErrorType.INVALID_PASSWORD;
        }
        return AuthenticationErrorType.UNAUTHORIZED;
    }

    /**
     * 표준화된 에러 응답을 HTTP 응답으로 전송
     *
     * @param response  HTTP 응답 객체
     * @param errorType 전송할 에러 타입
     * @throws IOException 응답 작성 중 발생할 수 있는 예외
     */
    private void sendErrorResponse(HttpServletResponse response, AuthenticationErrorType errorType) throws IOException {
        // 응답 헤더 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorType.getStatus());

        // 에러 응답 객체 생성
        ErrorResponse errorResponse = ErrorResponse.builder()
                .success(false)
                .message(errorType.getMessage())
                .code(errorType.getCode())
                .errors(Collections.emptyList())
                .build();

        // JSON 형태로 에러 응답 전송
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    /**
     * 인증 관련 에러 유형을 정의하는 열거형
     * 각 에러 유형별로 HTTP 상태 코드, 메시지, 에러 코드 정의
     */
    private enum AuthenticationErrorType {
        /**
         * 사용자를 찾을 수 없는 경우
         */
        AGENT_NOT_FOUND(HttpServletResponse.SC_BAD_REQUEST, "해당 이메일의 사용자를 찾을 수 없습니다.", "AGENT_NOT_FOUND"),

        /**
         * 비밀번호가 일치하지 않는 경우
         */
        INVALID_PASSWORD(HttpServletResponse.SC_BAD_REQUEST, "비밀번호가 일치하지 않습니다.", "INVALID_PASSWORD"),

        /**
         * 일반적인 인증 실패
         */
        UNAUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "인증되지 않은 사용자입니다.", "UNAUTHORIZED"),

        /**
         * 접근 권한 부족
         */
        FORBIDDEN(HttpServletResponse.SC_FORBIDDEN, "해당 리소스에 접근할 권한이 없습니다.", "FORBIDDEN");

        /**
         * HTTP 상태 코드
         */
        private final int status;

        /**
         * 사용자에게 표시될 메시지
         */
        private final String message;

        /**
         * 내부적으로 사용될 에러 코드
         */
        private final String code;

        /**
         * 에러 타입 생성자
         *
         * @param status  HTTP 상태 코드
         * @param message 에러 메시지
         * @param code    에러 코드
         */
        AuthenticationErrorType(int status, String message, String code) {
            this.status = status;
            this.message = message;
            this.code = code;
        }

        /**
         * HTTP 상태 코드 반환
         */
        public int getStatus() {
            return status;
        }

        /**
         * 에러 메시지 반환
         */
        public String getMessage() {
            return message;
        }

        /**
         * 에러 코드 반환
         */
        public String getCode() {
            return code;
        }
    }
}