package com.househub.backend.domain.notification.service.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.notification.service.EmailContentProvider;

@Component
public class PasswordResetEmailContentProvider implements EmailContentProvider {

	@Override
	public String getSubject() {
		return "HouseHub 비밀번호 재설정 안내";
	}

	@Override
	public String getBody(Map<String, String> variables) {
		String resetLink = variables.get("resetLink");
		return String.format("""
			<!DOCTYPE html>
			<html>
			<body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
				<p>안녕하세요 <strong>HouseHub</strong> 사용자님,</p>

				<p>비밀번호 재설정 요청을 확인했습니다.<br>
				아래 버튼을 클릭하시면 새로운 비밀번호를 설정하실 수 있습니다.</p>

				<p style="text-align: center; margin: 30px 0;">
					<a href="%s" style="
						display: inline-block;
						padding: 12px 24px;
						font-size: 16px;
						color: #ffffff;
						background-color: #4CAF50;
						text-decoration: none;
						border-radius: 6px;">
						비밀번호 재설정하기
					</a>
				</p>

				<p style="font-size: 14px; color: #777;">
					본 링크는 보안을 위해 <strong>30분간만 유효</strong>합니다.<br>
					링크가 만료된 경우, 다시 요청해 주세요.
				</p>

				<p>감사합니다.<br>
				<span style="font-weight: bold;">HouseHub 팀 드림</span></p>
			</body>
			</html>
			""", resetLink);
	}
}

