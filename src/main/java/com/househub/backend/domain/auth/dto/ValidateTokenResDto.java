package com.househub.backend.domain.auth.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidateTokenResDto {
	private boolean valid;
	private LocalDateTime expiresAt;
	private String email; // 필요 시 추가 정보 반환
	private LocalDateTime usedAt;
}
