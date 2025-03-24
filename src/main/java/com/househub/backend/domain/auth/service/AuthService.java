package com.househub.backend.domain.auth.service;

import com.househub.backend.domain.auth.dto.SignUpRequestDto;

public interface AuthService {
    public void signup(SignUpRequestDto request);
}
