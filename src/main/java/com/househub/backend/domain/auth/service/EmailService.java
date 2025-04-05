package com.househub.backend.domain.auth.service;

public interface EmailService {
	void sendVerificationCode(String email, String verificationCode);
}