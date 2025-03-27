package com.househub.backend.domain.auth.service;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.auth.dto.SignInReqDto;
import com.househub.backend.domain.auth.dto.SignInResDto;
import com.househub.backend.domain.auth.exception.AgentNotFoundException;
import com.househub.backend.domain.auth.exception.InvalidPasswordException;
import com.househub.backend.domain.auth.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SignInServiceTest {
    @Mock
    private AgentRepository agentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private Agent agent;
    private SignInReqDto signInReqDto;

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
    }

    @Test
    @DisplayName("로그인 성공")
    void signin_success() {
        when(agentRepository.findByEmail(signInReqDto.getEmail())).thenReturn(Optional.of(agent));
        when(passwordEncoder.matches(signInReqDto.getPassword(), agent.getPassword())).thenReturn(true);

        SignInResDto result = authService.signin(signInReqDto);

        assertNotNull(result);
        assertEquals(agent.getId(), result.getId());
        assertEquals(agent.getEmail(), result.getEmail());
    }

    @Test
    @DisplayName("로그인 실패 - 사용자를 찾을 수 없음")
    void signin_fail_agentNotFound() {
        when(agentRepository.findByEmail(signInReqDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(AgentNotFoundException.class, () -> authService.signin(signInReqDto));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void signin_fail_invalidPassword() {
        when(agentRepository.findByEmail(signInReqDto.getEmail())).thenReturn(Optional.of(agent));
        when(passwordEncoder.matches(signInReqDto.getPassword(), agent.getPassword())).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> authService.signin(signInReqDto));
    }
}
