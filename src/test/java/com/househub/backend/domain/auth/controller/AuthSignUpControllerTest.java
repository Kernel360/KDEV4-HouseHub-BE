package com.househub.backend.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.househub.backend.common.config.WebSecurityConfig;
import com.househub.backend.domain.auth.dto.SignUpReqDto;
import com.househub.backend.domain.auth.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = AuthController.class)
@Import(WebSecurityConfig.class)
public class AuthSignUpControllerTest {

    @MockitoBean
    private AuthService authService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 실패 - AgentDto 유효성 검사")
    void signup_fail_agent_dto_validation() throws Exception {
        SignUpReqDto request = SignUpReqDto.builder()
                .agent(SignUpReqDto.AgentDto.builder()
                        .name("") // 이름 누락
                        .licenseNumber("잘못된 자격증 번호")
                        .email("잘못된 이메일 번호")
                        .password("1234") // 짧은 비밀번호
                        .contact("잘못된 연락처")
                        .build())
                .realEstate(null) // 부동산 정보는 null 로 설정
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // RealEstateDto 는 선택값인데 입력받는 경우,
    @Test
    @DisplayName("회원가입 실패 - RealEstateDto 유효성 검사")
    void signup_fail_realestate_dto_validation() throws Exception {
        SignUpReqDto request = SignUpReqDto.builder()
                .agent(SignUpReqDto.AgentDto.builder()
                        .name("테스트 에이전트")
                        .licenseNumber("서울-2023-12345")
                        .email("test@example.com")
                        .password("password123!")
                        .contact("010-1234-5678")
                        .build())
                .realEstate(SignUpReqDto.RealEstateDto.builder()
                        .name("") // 부동산 이름 누락
                        .businessRegistrationNumber("잘못된 사업자등록번호")
                        .address("") // 지번 주소 누락
                        .roadAddress("") // 도로명 주소 누락
                        .contact("잘못된 연락처")
                        .build())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 성공 - 부동산 정보 입력 안 한 경우")
    void signup_success_without_realestate() throws Exception {
        SignUpReqDto request = SignUpReqDto.builder()
                .agent(SignUpReqDto.AgentDto.builder()
                        .name("테스트 에이전트")
                        .licenseNumber("서울-2023-12345")
                        .email("test@example.com")
                        .password("password123!")
                        .contact("010-1234-5678")
                        .build())
                .realEstate(null) // 부동산 정보 누락
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("회원가입 성공 - 부동산 정보 입력한 경우")
    void signup_success_include_realestate() throws Exception {
        SignUpReqDto request = SignUpReqDto.builder()
                .agent(SignUpReqDto.AgentDto.builder()
                        .name("테스트 에이전트")
                        .licenseNumber("서울-2023-12345")
                        .email("test@example.com")
                        .password("password123!")
                        .contact("010-1234-5678")
                        .build())
                .realEstate(SignUpReqDto.RealEstateDto.builder()
                        .name("테스트 부동산")
                        .businessRegistrationNumber("123-45-67890")
                        .address("서울시 강남구")
                        .roadAddress("서울시 강남구 도로명")
                        .contact("02-1234-5678")
                        .build())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
