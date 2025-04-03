package com.househub.backend.domain.auth.dto;

import com.househub.backend.domain.agent.entity.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetSessionResDto {
	private Long id; // 사용자 식별자
	private String name; // 사용자 이름
	private String email; // 사용자 이메일
	private Role role; // 사용자 권한(ADMIN, AGENT)
	private boolean isAuthenticated; // 인증 여부
	private String expiresAt; // 세션 만료 시간
}
