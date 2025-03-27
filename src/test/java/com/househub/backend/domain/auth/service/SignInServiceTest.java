package com.househub.backend.domain.auth.service;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.auth.dto.SignInReqDto;
import com.househub.backend.domain.auth.dto.SignInResDto;
import com.househub.backend.domain.auth.exception.InvalidPasswordException;
import com.househub.backend.domain.auth.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignInServiceTest {
    @Mock
    private AgentRepository agentRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private Agent agent;
    private SignInReqDto signInReqDto;
    private MockHttpSession mockHttpSession;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        agent = Agent.builder()
                .id(1L)
                .email("gongil@example.com")
                .password("encodedPassword")
                .build();

        signInReqDto = SignInReqDto.builder()
                .email("gongil@example.com")
                .password("password123!")
                .build();

        mockHttpSession = new MockHttpSession();
        authentication = mock(Authentication.class); // authentication mock 생성
    }

    @Test
    @DisplayName("로그인 성공")
    void signin_success() {
        when(agentRepository.findByEmail(signInReqDto.getEmail())).thenReturn(Optional.of(agent));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        SignInResDto result = authService.signin(signInReqDto, mockHttpSession);

        assertNotNull(result);
        assertEquals(agent.getId(), result.getId());
        assertEquals(agent.getEmail(), result.getEmail());
        assertEquals(agent.getName(), mockHttpSession.getAttribute("agentName")); // 세션 속성 검증
        assertEquals(agent.getId(), mockHttpSession.getAttribute("agentId")); // 세션 속성 검증
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("로그인 실패 - 사용자를 찾을 수 없음")
    void signin_fail_agentNotFound() {
        when(agentRepository.findByEmail(signInReqDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.signin(signInReqDto, mockHttpSession));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void signin_fail_invalidPassword() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(InvalidPasswordException.class);

        assertThrows(InvalidPasswordException.class, () -> authService.signin(signInReqDto, mockHttpSession));
    }
}
