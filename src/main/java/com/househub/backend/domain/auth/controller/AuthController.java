package com.househub.backend.domain.auth.controller;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.auth.dto.SignUpRequestDto;
import com.househub.backend.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        return ResponseEntity.ok(SuccessResponse.success("회원가입 신청이 완료되었습니다. 관리자 승인까지 시간이 소요될 수 있습니다.", "SIGNUP_SUCCESS", null));
    }
}
