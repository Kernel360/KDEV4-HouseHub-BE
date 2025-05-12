package com.househub.backend.domain.auth.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.service.AgentReader;
import com.househub.backend.domain.agent.service.AgentStore;
import com.househub.backend.domain.notification.dto.EmailReqDto;
import com.househub.backend.domain.notification.service.EmailService;
import com.househub.backend.domain.notification.service.impl.PasswordResetEmailContentProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
	private final AgentReader agentReader;
	private final AgentStore agentStore;
	private final PasswordResetTokenManager tokenManager;
	private final EmailService emailService;
	private final PasswordResetEmailContentProvider contentProvider;
	private final PasswordEncoder passwordEncoder;

	@Value("${app.frontend.url}")
	private String frontendUrl;

	/**
	 * 1) 이메일로 비밀번호 재설정 요청
	 * 2) 유저 확인 → 토큰 생성/저장 → 이메일 발송
	 */
	public void sendPasswordResetEmail(String email) {
		// --- 1. 유저 존재 검증 ---
		Agent agent = agentReader.findByEmail(email);

		// --- 2. 토큰 생성 및 Redis 저장 (30분 TTL) ---
		String token = tokenManager.createToken(agent.getId());

		// --- 3. 리셋 링크 조립 ---
		String resetLink = frontendUrl + "/reset-password/" + token;
		Map<String, String> variables = Map.of("resetLink", resetLink);

		// --- 4. 이메일 제목/본문 생성 ---
		String subject = contentProvider.getSubject();
		String body = contentProvider.getBody(variables);

		// --- 5. EmailReqDto 조립 & 전송 ---
		EmailReqDto emailRequest = EmailReqDto.builder()
			.to(email)
			.subject(subject)
			.body(body)
			.build();

		// --- 6. 이메일 전송 ---
		emailService.sendEmail(emailRequest);
	}

	@Transactional
	public void resetPassword(String token, String newPassword) {
		Long agentId = tokenManager.extractAgentIdFromToken(token);
		Agent agent = agentReader.findById(agentId);
		agent.updatePassword(passwordEncoder.encode(newPassword));
		agentStore.update(agent);
		// Optional: 토큰 무효화 (예: Redis에서 삭제 등)
		tokenManager.invalidateToken(token);
	}
}

