package com.househub.backend.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.househub.backend.common.config.WebSecurityConfig;
import com.househub.backend.domain.auth.dto.SignInReqDto;
import com.househub.backend.domain.auth.dto.SignInResDto;
import com.househub.backend.domain.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@Import(WebSecurityConfig.class)
public class AuthSignInControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private SignInResDto signInAgent;
    private SignInReqDto validSignInReqDto;

    @BeforeEach
    void setUp() {
        signInAgent = SignInResDto.builder()
            .id(1L)
            .email("test@example.com")
            .build();

        validSignInReqDto = SignInReqDto
            .builder()
            .email("test@example.com")
            .password("password123!!!")
            .build();

        SignInReqDto invalidSignInReqDto = SignInReqDto
                .builder()
                .email("wrong@example.com")
                .password("wrongpassword")
                .build();
    }

    @Test
    @DisplayName("로그인 성공 - 세션 생성 및 응답")
    void login_success() throws Exception {
        when(authService.signin(validSignInReqDto)).thenReturn(signInAgent);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validSignInReqDto)).with(SecurityMockMvcRequestPostProcessors.csrf())) // CSRF 토큰 추가)
            .andExpect(status().isOk()) // 응답 상태가 200 OK 인지 확인
            .andExpect(jsonPath("$.success").value(true));
    }

}
