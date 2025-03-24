package com.househub.backend.domain.auth.controller;

import com.househub.backend.domain.auth.dto.SignUpRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @PostMapping("/signup")
    public String signup(@Valid @RequestBody SignUpRequest request) {
        log.info("{}", request.getAgent());
        log.info("{}", request.getRealEstate());
        return "부동산 공인중개사 회원가입";
    }
}
