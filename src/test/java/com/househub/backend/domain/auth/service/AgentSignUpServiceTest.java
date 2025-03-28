package com.househub.backend.domain.auth.service;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.RealEstate;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.auth.dto.SignUpReqDto;
import com.househub.backend.domain.auth.exception.EmailVerifiedException;
import com.househub.backend.domain.auth.service.impl.AuthServiceImpl;
import com.househub.backend.domain.realEstate.repository.RealEstateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgentSignUpServiceTest {
    @Mock
    private AgentRepository agentRepository;

    @Mock
    private RealEstateRepository realEstateRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private SignUpReqDto request;
    private SignUpReqDto.AgentDto agentDto;

    // 테스트에 필요한 데이터 초기화
    @BeforeEach
    void setup() {
        agentDto = SignUpReqDto.AgentDto.builder()
                .name("박공일")
                .licenseNumber("서울-2023-12345")
                .email("gongil@example.com")
                .password("password123!")
                .contact("010-1234-5678")
                .isEmailVerified(true) // 이메일 인증 여부 설정
                .build();

        SignUpReqDto.RealEstateDto realEstateDto = SignUpReqDto.RealEstateDto.builder()
                .name("테스트 부동산")
                .businessRegistrationNumber("123-45-67890")
                .address("테스트 주소")
                .roadAddress("테스트 도로명 주소")
                .contact("02-1234-5678")
                .build();

        request = SignUpReqDto.builder()
                .agent(agentDto)
                .realEstate(realEstateDto)
                .build();
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 미인증")
    void signup_fail_email_not_verified() {
        // 이메일 인증되지 않은 경우
        agentDto.setEmailVerified(false);

        // 이메일 인증되지 않았을 때 예외가 발생하는지 확인
        assertThrows(EmailVerifiedException.class, () -> authService.signup(request));

        // 아무것도 호출되지 않았는지 검증
        verify(agentRepository, never()).findByEmail(any());
        verify(realEstateRepository, never()).findByBusinessRegistrationNumber(any());
        verify(realEstateRepository, never()).save(any());
        verify(agentRepository, never()).save(any());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void signup_fail_email_duplicate() {
        // 이메일 중복 검사를 위해 이미 존재하는 이메일 반환
        when(agentRepository.findByEmail(any())).thenReturn(Optional.of(Agent.builder().email("gongil@example.com").build()));

        // 이메일 중복 예외 발생 확인
        assertThrows(AlreadyExistsException.class, () -> authService.signup(request));

        // agentrepository.findByEmail 호출되었는지 검증
        verify(agentRepository, times(1)).findByEmail(any());
        verify(realEstateRepository, never()).findByBusinessRegistrationNumber(any());
        verify(realEstateRepository, never()).save(any());
        verify(agentRepository, never()).save(any());
    }

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() {
        // 정상적인 회원가입
        when(agentRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(realEstateRepository.findByBusinessRegistrationNumber(any())).thenReturn(Optional.empty());
        when(realEstateRepository.save(any())).thenReturn(RealEstate.builder().build());
        when(agentRepository.save(any())).thenReturn(Agent.builder().build());

        authService.signup(request);

        // 각 리포지토리 메서드가 예상되로 호출되었는지 검증
        verify(agentRepository, times(1)).findByEmail(any());
        verify(realEstateRepository, times(1)).save(any());
        verify(agentRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("회원가입 실패 - 자격증번호 중복")
    void signup_fail_licensenumber_duplicate() {
        // 자격증 번호를 선택적으로 입력했는데 이미 존재하는 경우
        when(agentRepository.findByLicenseNumber(any())).thenReturn(Optional.of(Agent.builder().licenseNumber("서울-2023-12345").build()));

        // AlreadyExistException 예외 발생했는지 검증
        assertThrows(AlreadyExistsException.class, () -> authService.signup(request));

        // 자격증 번호 중복 확인 메서드만 호출되었는지 검증
        verify(agentRepository, times(1)).findByLicenseNumber(any());
        verify(realEstateRepository, never()).findByBusinessRegistrationNumber(any());
        verify(realEstateRepository, never()).save(any());
        verify(agentRepository, never()).save(any());
    }
}
