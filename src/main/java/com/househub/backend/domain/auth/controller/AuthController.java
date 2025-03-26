package com.househub.backend.domain.auth.controller;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.auth.dto.SignUpRequestDto;
import com.househub.backend.domain.auth.exception.EmailVerifiedException;
import com.househub.backend.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<Void>> signup(@Valid @RequestBody SignUpRequestDto request) {
        log.info("{}", request.getAgent());
        log.info("{}", request.getRealEstate());

        authService.signup(request);

        return ResponseEntity.ok(SuccessResponse.success("회원가입 성공.", "SIGNUP_SUCCESS", null));
    }

    @ExceptionHandler(EmailVerifiedException.class)
    public ResponseEntity<EmailVerifiedException> handleEmailNotVerifiedException(EmailVerifiedException ex) {
        return ResponseEntity.badRequest().body(ex);
    }
}
