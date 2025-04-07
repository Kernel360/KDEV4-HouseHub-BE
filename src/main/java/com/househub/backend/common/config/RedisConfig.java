package com.househub.backend.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Redis 설정 클래스
 * Redis 연결 및 RedisTemplate 빈 설정 담당
 */
@Slf4j
@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfig {
	@Value("${spring.data.redis.host}")
	private String host;

	@Value("${spring.data.redis.port}")
	private int port;

	/**
	 * Redis 연결 팩토리 빈 생성
	 * Lettuce를 사용하여 Redis 연결 생성.
	 *
	 * @return RedisConnectionFactory Redis 연결 팩토리
	 */
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		log.info("Redis host: {}, port: {}", host, port);
		return new LettuceConnectionFactory(host, port);
	}

	/**
	 * RedisTemplate 빈 생성
	 * Redis에 데이터를 저장하고 조회하는 데 사용되는 템플릿
	 * 키와 값을 String 타입으로 직렬화/역직렬화하도록 설정
	 *
	 * @return RedisTemplate Redis 템플릿
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer()); // 키 직렬화/역직렬화 설정
		redisTemplate.setValueSerializer(new StringRedisSerializer()); // 값 직렬화/역직렬화 설정
		redisTemplate.setConnectionFactory(redisConnectionFactory()); // 연결 팩토리 설정
		return redisTemplate;
	}
}
