package com.househub.backend.domain.auth.service.impl;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.househub.backend.domain.auth.service.EmailAuthCodeManager;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailAuthCodeManagerImpl implements EmailAuthCodeManager {
	private final RedisTemplate<String, String> redisTemplate;

	/**
	 * 6자리 숫자 조합의 인증 코드 생성
	 *
	 * @return 생성된 인증 코드
	 */
	@Override
	public String generateAuthCode() {
		Random random = new Random();
		StringBuilder authCode = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			authCode.append(random.nextInt(10));
		}
		return authCode.toString();
	}

	/**
	 * 생성된 인증 코드를 Redis에 저장.
	 *
	 * @param email    사용자 이메일
	 * @param authCode 인증 코드
	 */
	@Override
	public void saveAuthCode(String email, String authCode) {
		redisTemplate.opsForValue().set(email, authCode, 3, TimeUnit.MINUTES);
	}

	/**
	 * Redis 에서 인증 코드 조회.
	 *
	 * @param email 사용자 이메일
	 * @return 조회된 인증 코드 (null 이면 인증 코드가 존재하지 않음)
	 */
	@Override
	public String getAuthCode(String email) {
		return redisTemplate.opsForValue().get(email);
	}

	/**
	 * Redis 에서 인증 코드 삭제.
	 *
	 * @param email 사용자 이메일
	 */
	@Override
	public void deleteAuthCode(String email) {
		redisTemplate.delete(email);
	}

	/**
	 * 인증 코드 생성 후 Redis에 저장하고, 생성된 인증 코드를 반환합니다.
	 *
	 * @param email 사용자 이메일
	 * @return 생성된 인증 코드
	 */
	@Override
	public String generateAndSaveCode(String email) {
		String authCode = generateAuthCode();
		saveAuthCode(email, authCode);
		return authCode;
	}
}
