package com.househub.backend.domain.inquiryTemplates.service;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.RealEstate;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.auth.dto.SignUpReqDto;
import com.househub.backend.domain.auth.exception.EmailVerifiedException;
import com.househub.backend.domain.auth.service.impl.AuthServiceImpl;
import com.househub.backend.domain.inquiryForm.dto.CreateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryForm.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryForm.entity.Question;
import com.househub.backend.domain.inquiryForm.entity.QuestionType;
import com.househub.backend.domain.inquiryForm.repository.InquiryTemplateRepository;
import com.househub.backend.domain.inquiryForm.repository.QuestionRepository;
import com.househub.backend.domain.inquiryForm.service.impl.InquiryTemplateServiceImpl;
import com.househub.backend.domain.realEstate.repository.RealEstateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InquiryTemplateServiceTest {
    @Mock
    private InquiryTemplateRepository inquiryTemplateRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private InquiryTemplateServiceImpl inquiryTemplateService;

    private CreateInquiryTemplateReqDto reqDto;
    private CreateInquiryTemplateReqDto.QuestionDto questionDto;

    // 테스트에 필요한 데이터 초기화
    @BeforeEach
    void setup() {
        // 테스트 데이터 생성
        CreateInquiryTemplateReqDto.QuestionDto questionDto1 = CreateInquiryTemplateReqDto.QuestionDto.builder()
                .label("이름")
                .type(QuestionType.TEXT)
                .required(true)
                .questionOrder(1)
                .build();

        CreateInquiryTemplateReqDto.QuestionDto questionDto2 = CreateInquiryTemplateReqDto.QuestionDto.builder()
                .label("이메일")
                .type(QuestionType.TEXT)
                .required(true)
                .questionOrder(2)
                .build();

        List<CreateInquiryTemplateReqDto.QuestionDto> questionDtos = Arrays.asList(questionDto1, questionDto2);

        reqDto = CreateInquiryTemplateReqDto.builder()
                .name("고객 문의 템플릿")
                .description("고객 문의 시 사용하는 템플릿입니다.")
                .questions(questionDtos)
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("문의 템플릿 등록 성공")
    void createNewInquiryTemplate_success() {
        // given: 테스트에 필요한 객체와 데이터를 설정
        // DTO를 엔티티로 변환하는 로직 모방
        InquiryTemplate inquiryTemplate = InquiryTemplate.fromDto(reqDto);
        List<Question> questions = reqDto.getQuestions().stream()
            .map(questionDto -> Question.fromDto(questionDto, inquiryTemplate))
            .collect(Collectors.toList());

        // Mock 객체의 행위 정의
        when(inquiryTemplateRepository.save(any(InquiryTemplate.class))).thenReturn(inquiryTemplate);
        when(questionRepository.saveAll(anyList())).thenReturn(questions);

        // when: 테스트 대상 메서드를 실행합니다.
        inquiryTemplateService.createNewInquiryTemplate(reqDto);

        // then: 메서드 실행 결과 검증.
        verify(inquiryTemplateRepository, times(1)).save(any(InquiryTemplate.class));
        verify(questionRepository, times(1)).saveAll(anyList());
    }
}
