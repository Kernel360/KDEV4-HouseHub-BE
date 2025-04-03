package com.househub.backend.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

// 세션 관리 담당
@Component
public class SessionManager {
	public void manageAgentSession(Authentication authentication) {
		// 보안 컨텍스트 설정
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// 세션 관리 로직
		HttpSession session = getCurrentSession();
		SecurityContext securityContext = SecurityContextHolder.getContext();

		// 세션 발급
		session.setAttribute(
			HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
			securityContext
		);
		session.setMaxInactiveInterval(3600); // 1시간
	}

	private HttpSession getCurrentSession() {
		ServletRequestAttributes attributes =
			(ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		return attributes.getRequest().getSession(true);
	}

	public static String getSessionExpirationTime(int maxInactiveInterval) {
		LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(maxInactiveInterval);
		ZonedDateTime zonedDateTime = expiresAt.atZone(ZoneId.systemDefault());
		return zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	}
}
