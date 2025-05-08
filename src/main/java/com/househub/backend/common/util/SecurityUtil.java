package com.househub.backend.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.househub.backend.common.exception.UnauthorizedException;
import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.auth.dto.CustomAgentDetails;

public class SecurityUtil {

	private SecurityUtil() {
	}

	public static AgentResDto getAuthenticatedAgent() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new UnauthorizedException("인증된 사용자가 아닙니다.", "UNAUTHORIZED_ACCESS");
		}

		Object principal = authentication.getPrincipal();

		if (principal instanceof CustomAgentDetails) {
			CustomAgentDetails customAgentDetails = (CustomAgentDetails)principal;
			return customAgentDetails.getAgentDetailsDto();
		}

		throw new UnauthorizedException("잘못된 인증 정보입니다.", "INVALID_CREDENTIALS");
	}
}

