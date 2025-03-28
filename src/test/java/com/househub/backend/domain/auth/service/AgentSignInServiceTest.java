package com.househub.backend.domain.auth.service;

// 필수 Import 목록
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// 스프링 시큐리티 관련 Import
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

// 로컬 도메인 Import
import com.househub.backend.domain.auth.dto.SignInReqDto;
import com.househub.backend.domain.auth.dto.SignInResDto;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.common.util.SessionManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.househub.backend.domain.auth.service.impl.AuthServiceImpl;
import com.househub.backend.domain.realEstate.repository.RealEstateRepository;

// 테스트 라이브러리 Import
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AgentSignInServiceTest {
    @Mock
    private AgentRepository agentRepository;

    @Mock
    private RealEstateRepository realEstateRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private SessionManager sessionManager;

    // 반드시 인터페이스가 아닌 구현체 타입 지정
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @DisplayName("로그인 성공")
    void signin_success_case() {
        // Given: 로그인 요청 데이터 준비
        SignInReqDto request = SignInReqDto.builder()
            .email("test@example.com")
            .password("password123!")
            .build();
        // 모의 에이전트 객체 생성
        Agent mockAgent = Agent.builder()
            .id(1L)
            .email("test@example.com")
            .build();

        // 모의 인증 토큰 생성
        Authentication mockAuthentication = new UsernamePasswordAuthenticationToken(
            request.getEmail(), request.getPassword()
        );

        // Mocking: 인증 관리자 동작 정의
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuthentication);

        // Mocking: 저장소에서 사용자 조회 동작 정의
        when(agentRepository.findByEmail(request.getEmail()))
            .thenReturn(Optional.of(mockAgent));

        // When: 로그인 서비스 호출
        SignInResDto result = authService.signin(request);

        // Then: 결과 검증
        assertThat(result.getEmail()).isEqualTo(request.getEmail());
        verify(sessionManager).manageAgentSession(mockAuthentication);
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 사용자")
    void signin_fail_agent_not_found() {
        // Given: 존재하지 않는 이메일로 로그인 요청
        SignInReqDto request = SignInReqDto.builder()
            .email("notexist@example.com")
            .password("password123!")
            .build();

        // Mocking: 인증 관리자 동작 정의
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        // When, Then: 리소스 미존재 예외 발생 검증
        assertThrows(ResourceNotFoundException.class, () -> {
            authService.signin(request);
        });
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void signin_fail_invalid_password() {
        // Given: 잘못된 비밀번호로 로그인 요청
        SignInReqDto request = SignInReqDto.builder()
                .email("test@example.com")
                .password("password123!")
                .build();

        // Mocking: 인증 관리자 동작 정의
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("비밀번호가 일치하지 않습니다."));

        // When, Then: 잘못된 자격 증명 예외 발생 검증
        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authService.signin(request)
        );

        // 예외 메시지 검증
        assertThat(exception.getMessage()).contains("비밀번호가 일치하지 않습니다.");

        // 세션 관리자가 호출되지 않았음을 검증
        verify(sessionManager, never()).manageAgentSession(any());
    }
}
