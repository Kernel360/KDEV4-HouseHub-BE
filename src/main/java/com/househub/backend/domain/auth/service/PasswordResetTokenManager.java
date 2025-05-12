package com.househub.backend.domain.auth.service;

import java.time.Duration;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordResetTokenManager {

	private final RedisTemplate<String, String> redisTemplate;

	public String createToken(Long userId) {
		String token = UUID.randomUUID().toString();
		redisTemplate.opsForValue().set(getKey(token), userId.toString(), Duration.ofMinutes(30));
		return token;
	}

	public Long extractAgentIdFromToken(String token) {
		String agentIdStr = redisTemplate.opsForValue().get(getKey(token));
		if (agentIdStr == null)
			throw new BusinessException(ErrorCode.INVALID_OR_EXPIRED_TOKEN);
		return Long.valueOf(agentIdStr);
	}

	public void invalidateToken(String token) {
		redisTemplate.delete(getKey(token));
	}

	private String getKey(String token) {
		return "password-reset:" + token;
	}
}
