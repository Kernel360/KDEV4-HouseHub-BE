package com.househub.backend.domain.auth.service;

import com.househub.backend.domain.auth.dto.SignInReqDto;
import com.househub.backend.domain.auth.dto.SignInResDto;
import com.househub.backend.domain.auth.dto.SignUpReqDto;

public interface AuthService {
	void signup(SignUpReqDto request);

	SignInResDto signin(SignInReqDto request);

	public String generateAndSaveAuthCode(String email);

	public String getAuthCode(String email);

	public void deleteAuthCode(String email);

	public void verifyCode(String email, String code);
}
