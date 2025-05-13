package com.househub.backend.domain.auth.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
import com.househub.backend.domain.agent.service.AgentReader;
import com.househub.backend.domain.notification.dto.EmailReqDto;
import com.househub.backend.domain.notification.service.EmailService;
import com.househub.backend.domain.notification.service.impl.EmailVerificationContentProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
	private final AgentReader agentReader;
	private final EmailAuthCodeManager authCodeManager;
	private final EmailService emailService;
	private final EmailVerificationContentProvider contentProvider;

	/**
	 * 1) 이메일로 인증 코드 발송
	 * 2) 유저 확인 → 인증 코드 생성/저장 → 이메일 발송
	 */
	public void sendEmailVerificationCode(String email) {
		// --- 1. 유저 존재 검증 ---
		boolean isEmailAlreadyExist = agentReader.existsByEmail(email);
		if (isEmailAlreadyExist) {
			throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
		}

		// --- 2. 인증 코드 생성 및 Redis 저장 (3분 TTL 등) ---
		String code = authCodeManager.generateAndSaveCode(email);

		// --- 3. 이메일 내용 조립 ---
		Map<String, String> variables = Map.of("verificationCode", code);
		String subject = contentProvider.getSubject();
		String body = contentProvider.getBody(variables);

		// --- 4. EmailReqDto 조립 & 전송 ---
		EmailReqDto emailRequest = EmailReqDto.builder()
			.to(email)
			.subject(subject)
			.body(body)
			.build();

		// --- 5. 이메일 전송 ---
		emailService.sendEmail(emailRequest);
	}
}
