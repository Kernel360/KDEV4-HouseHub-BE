package com.househub.backend.domain.notification.service.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.notification.service.EmailContentProvider;

@Component
public class EmailVerificationContentProvider implements EmailContentProvider {

	private static final String SUBJECT = "HouseHub 인증 코드 안내";

	@Override
	public String getSubject() {
		return SUBJECT;
	}

	@Override
	public String getBody(Map<String, String> variables) {
		String verificationCode = variables.get("verificationCode");

		return String.format("""
			<!DOCTYPE html>
			<html>
			<body style="font-family: Arial, sans-serif; line-height: 1.6;">
				<p>안녕하세요!</p>

				<p>HouseHub에 가입해 주셔서 감사합니다!</p>

				<p>서비스를 이용하시려면 아래의 인증 코드를 입력해 주세요:</p>

				<p style="font-size: 20px; font-weight: bold; color: #2E86C1; text-align: center;">
					인증 코드: %s
				</p>

				<p>이 인증 코드는 서비스 가입을 완료하는 데 필요하며, 한 번만 사용 가능합니다.</p>

				<p>감사합니다!</p>

				<p>HouseHub 팀 드림</p>
			</body>
			</html>
			""", verificationCode);
	}
}
