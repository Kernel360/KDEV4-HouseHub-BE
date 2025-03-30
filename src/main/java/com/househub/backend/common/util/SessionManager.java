package com.househub.backend.common.util;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest().getSession(true);
    }
}
