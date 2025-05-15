package com.househub.backend.domain.notification.service.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.notification.service.EmailContentProvider;

@Component
public class NewInquiryCreatedContentProvider implements EmailContentProvider {

	private static final String SUBJECT = "새로운 문의가 도착했습니다";

	@Override
	public String getSubject() {
		return SUBJECT;
	}

	@Override
	public String getBody(Map<String, String> variables) {
		// 이름이 없다면 "고객님"으로 통일
		String inquiryUrl = variables.getOrDefault("inquiryUrl", "#");

		return String.format("""
			<!DOCTYPE html>
			<html>
			<body style="font-family: Arial, sans-serif; line-height: 1.6;">
				<p><strong>고객님</strong>으로부터 새로운 문의가 도착했습니다.</p>

				<p>빠르게 응답해 주세요!</p>

				<p>
					<a href="%s" style="display: inline-block; padding: 10px 20px; background-color: #2E86C1; 
					color: white; text-decoration: none; border-radius: 5px;">
						문의 확인하러 가기
					</a>
				</p>

				<p>감사합니다.<br/>HouseHub 팀 드림</p>
			</body>
			</html>
			""", inquiryUrl);
	}
}
