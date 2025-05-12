package com.househub.backend.domain.auth.service;

public interface EmailAuthCodeManager {
	String generateAuthCode();

	void saveAuthCode(String email, String authCode);

	String getAuthCode(String email);

	void deleteAuthCode(String email);

	String generateAndSaveCode(String email);
}
