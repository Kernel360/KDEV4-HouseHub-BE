package com.househub.backend.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


/**
 * Redis 설정 클래스
 * Redis 연결 및 RedisTemplate 빈 설정 담당
 */
@Configuration
@EnableRedisRepositories
@EnableRedisHttpSession // Redis를 세션저장소로 사용
@RequiredArgsConstructor
public class RedisConfig {
    @Value("${spring.data.redis.host:localhost}")
    private String host;

    @Value("${spring.data.redis.port:6379}")
    private int port;

    /**
     * Redis 연결 팩토리 빈 생성
     * Lettuce를 사용하여 Redis 연결 생성.
     *
     * @return RedisConnectionFactory Redis 연결 팩토리
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
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
