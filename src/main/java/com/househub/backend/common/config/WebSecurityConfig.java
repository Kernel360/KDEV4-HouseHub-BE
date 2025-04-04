package com.househub.backend.common.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.househub.backend.common.exception.CustomAuthenticationHandler;
import com.househub.backend.common.response.SuccessResponse;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	private final ObjectMapper objectMapper;
	private final CustomAuthenticationHandler customAuthenticationHandler;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws
		Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.authorizeHttpRequests((authz) -> authz
				.requestMatchers(
					"/api/auth/session",
					"/api/auth/email/**",
					"/api/auth/signup",
					"/api/auth/signin",
					"/api/auth/signup",
					"/api/auth/signin",
					"/api/auth/email/**",
					"/v3/api-docs/**",
					"/swagger-ui/**",
					"/swagger-ui.html"
				).permitAll() // 공개 API
				.requestMatchers("/api/**").authenticated() // 인증 필요한 경로
			)
			.csrf(AbstractHttpConfigurer::disable) // REST API 서버이므로 CSRF 비활성화
			.exceptionHandling(configurer -> configurer
				.authenticationEntryPoint(customAuthenticationHandler)
				.accessDeniedHandler(customAuthenticationHandler)
			)
			.formLogin(form -> form.disable())
			.logout((logout) -> logout
				.logoutRequestMatcher(new AntPathRequestMatcher("/api/auth/logout"))
				.logoutSuccessHandler((request, response, authentication) -> {
					response.setStatus(HttpStatus.OK.value());
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					response.setCharacterEncoding("UTF-8");

					// SuccessResponse 객체 생성
					SuccessResponse successResponse = SuccessResponse.builder()
						.success(true)
						.message("로그아웃 성공")
						.code("LOGOUT_SUCCESS")
						.build();

					// SuccessResponse 객체를 JSON으로 변환하여 응답
					response.getWriter().write(objectMapper.writeValueAsString(successResponse));
				})
				.invalidateHttpSession(true)
				.deleteCookies("SESSION")
				.clearAuthentication(true)
			)
			.sessionManagement((sessionManagement) ->
					sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).maximumSessions(1)
				// 세션 기반 인증 사용
			);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080")); // 허용할 Origin
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
		configuration.setAllowedHeaders(
			Arrays.asList("Authorization", "Content-Type", "X-Requested-With")); // 허용할 Header
		configuration.setAllowCredentials(true); // 쿠키 허용
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/api/**", configuration); // /api/** 경로에 CORS 설정 적용
		return source;
	}
}