package com.househub.backend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable()) // REST API 서버이므로 CSRF 비활성화
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/api/**").permitAll() // /api/** 경로에 대한 모든 접근 허용
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .httpBasic(withDefaults()); // 기본 HTTP Basic 인증 사용 (선택 사항)
        return http.build();
    }
}