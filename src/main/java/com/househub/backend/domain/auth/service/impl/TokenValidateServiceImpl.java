package com.househub.backend.domain.auth.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.service.AgentReader;
import com.househub.backend.domain.auth.dto.ValidateTokenResDto;
import com.househub.backend.domain.auth.enums.TokenType;
import com.househub.backend.domain.auth.service.PasswordResetTokenManager;
import com.househub.backend.domain.auth.service.TokenValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenValidateServiceImpl implements TokenValidateService {
	private final PasswordResetTokenManager tokenManager;
	private final AgentReader agentReader;

	@Override
	public ValidateTokenResDto validate(String token, String type) {
		TokenType tokenType = TokenType.fromString(type); // TokenType enum으로 변환
		// 예시로 PASSWORD_RESET만 처리하는 경우
		if (tokenType == TokenType.PASSWORD_RESET) {
			Long agentId = tokenManager.extractAgentIdFromToken(token); // 유효성 체크 및 예외 처리 포함
			Agent agent = agentReader.findById(agentId);

			return ValidateTokenResDto.builder()
				.valid(true)
				.expiresAt(LocalDateTime.now().plusMinutes(30)) // Redis TTL 조회 가능 시 정확히 계산
				.email(agent.getEmail())
				.usedAt(null)
				.build();
		}

		throw new BusinessException(ErrorCode.INVALID_TOKEN_TYPE); // 여전히 처리하려면 예외 처리
	}
}
