package com.househub.backend.domain.consultation.service;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.consultation.dto.ConsultationReqDto;
import com.househub.backend.domain.consultation.dto.ConsultationResDto;
import com.househub.backend.domain.consultation.entity.Consultation;
import com.househub.backend.domain.consultation.enums.ConsultationStatus;
import com.househub.backend.domain.consultation.enums.ConsultationType;
import com.househub.backend.domain.consultation.repository.ConsultationRepository;
import com.househub.backend.domain.consultation.service.impl.ConsultationServiceImpl;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;


import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class ConsultationServiceTest {
    @InjectMocks
    private ConsultationServiceImpl consultationService;

    @Mock
    private ConsultationRepository consultationRepository;
    @Mock
    private AgentRepository agentRepository;
    @Mock
    private CustomerRepository customerRepository;

    private Consultation testConsultation;
    private ConsultationReqDto createRequestDto;
    private ConsultationReqDto updateRequestDto;
    private List<Consultation> testConsultations;
    private Agent agent;
    private Customer customer1;
    private Customer customer2;
    private Customer customer3;

    @BeforeEach
    void setup() {
        agent = createAgent();
        customer1 = createCustomer(1L, "customer1@example.com", "고객1", 10, "010-1111-5678", Gender.M);
        customer2 = createCustomer(2L, "customer2@example.com", "고객2", 10, "010-2222-5678", Gender.M);
        customer3 = createCustomer(3L, "customer3@example.com", "고객3", 10, "010-3333-5678", Gender.F);

        when(agentRepository.findById(agent.getId())).thenReturn(Optional.of(agent));
        when(customerRepository.findByIdAndAgentAndDeletedAtIsNull(customer1.getId(), agent))
                .thenReturn(Optional.of(customer1));
        when(customerRepository.findByIdAndAgentAndDeletedAtIsNull(customer2.getId(), agent))
                .thenReturn(Optional.of(customer2));
        when(customerRepository.findByIdAndAgentAndDeletedAtIsNull(customer3.getId(), agent))
                .thenReturn(Optional.of(customer3));

        testConsultation = Consultation.builder()
                .id(1L)
                .agent(agent)
                .customer(customer1)
                .consultationType(ConsultationType.PHONE)
                .content("신혼부부가 살만한 5억 이하의 아파트를 찾고 있습니다.")
                .consultationDate(LocalDateTime.now())
                .status(ConsultationStatus.RESERVED)
                .build();

        createRequestDto = ConsultationReqDto.builder()
                .agentId(1L)
                .customerId(1L)
                .consultationType(ConsultationType.PHONE)
                .content("신혼부부가 살만한 5억 이하의 아파트를 찾고 있습니다.")
                .consultationDate(LocalDateTime.of(2024, 3, 28, 15, 0))
                .status(ConsultationStatus.RESERVED)
                .build();

        updateRequestDto = ConsultationReqDto.builder()
                .agentId(1L)
                .customerId(1L)
                .consultationType(ConsultationType.VISIT)
                .content("수정된 상담 내용입니다.")
                .consultationDate(LocalDateTime.of(2024, 3, 30, 12, 0))
                .status(ConsultationStatus.COMPLETED)
                .build();

        testConsultations = List.of(
                testConsultation,
                Consultation.builder().id(2L).agent(agent).customer(customer2)
                        .consultationType(ConsultationType.PHONE)
                        .content("자취생이 살만한 1억 이하의 아파트를 찾고 있습니다.")
                        .consultationDate(LocalDateTime.now())
                        .status(ConsultationStatus.RESERVED)
                        .build(),
                Consultation.builder().id(3L).agent(agent).customer(customer3)
                        .consultationType(ConsultationType.VISIT)
                        .content("기혼부부가 살만한 10억 이하의 아파트를 찾고 있습니다.")
                        .consultationDate(LocalDateTime.now())
                        .status(ConsultationStatus.COMPLETED)
                        .build()
        );
    }

    private Agent createAgent() {
        return Agent.builder()
                .id(1L)
                .email("test@example.com")
                .name("김철수")
                .build();
    }

    private Customer createCustomer(Long id, String email, String name, int ageGroup, String contact, Gender gender) {
        return Customer.builder()
                .id(id)
                .email(email)
                .name(name)
                .ageGroup(ageGroup)
                .contact(contact)
                .gender(gender)
                .memo("메모")
                .deletedAt(null)
                .build();
    }

    @Test
    @DisplayName("상담 생성 성공")
    void createSuccess() {
        Long agentId = agent.getId();

        given(consultationRepository.save(any(Consultation.class)))
                .willReturn(testConsultation);

        ConsultationResDto result = consultationService.create(createRequestDto, agentId);

        assertNotNull(result);
        assertEquals(createRequestDto.getContent(), result.getContent());
    }

    @Test
    @DisplayName("상담 전체 조회 성공")
    void findAllSuccess() {
        Long agentId = agent.getId();

        given(consultationRepository.findAllByAgentAndDeletedAtIsNull(agent))
                .willReturn(testConsultations);

        List<ConsultationResDto> result = consultationService.findAll(agentId);

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("존재하지 않는 공인중개사로 상담 전체 조회 시 예외 발생")
    void findAllFail() {
        Long invalidAgentId = 999L;

        when(agentRepository.findById(invalidAgentId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> consultationService.findAll(invalidAgentId));

        assertEquals("공인중개사가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("상담 조회 성공")
    void findOneSuccess() {
        Long id = testConsultation.getId();
        Long agentId = agent.getId();

        given(consultationRepository.findByIdAndAgentAndDeletedAtIsNull(id, agent))
                .willReturn(Optional.of(testConsultation));

        ConsultationResDto result = consultationService.findOne(id, agentId);

        assertNotNull(result);
    }

    @Test
    @DisplayName("존재하지 않는 상담 아이디로 상담 조회 시 예외 발생")
    void findOneFailWithInvalidId() {
        Long invalidId = 999L;
        Long agentId = agent.getId();

        when(consultationRepository.findById(invalidId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> consultationService.findOne(invalidId, agentId));

        assertEquals("해당 상담 내역이 존재하지 않습니다:" + invalidId, exception.getMessage());
    }

    @Test
    @DisplayName("상담 삭제 성공")
    void deleteSuccess() {
        Long id = testConsultation.getId();
        Long agentId = agent.getId();

        given(consultationRepository.findByIdAndAgentAndDeletedAtIsNull(id, agent))
                .willReturn(Optional.of(testConsultation));

        ConsultationResDto result = consultationService.delete(id, agentId);

        assertNotNull(result.getDeletedAt());
    }

    @Test
    @DisplayName("존재하지 않는 공인중개사로 상담 삭제 시 예외 발생")
    void deleteFailWithInvalidAgent() {
        Long id = testConsultation.getId();
        Long invalidAgentId = 999L;

        when(agentRepository.findById(invalidAgentId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> consultationService.delete(id, invalidAgentId));

        assertEquals("공인중개사가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("존재하지 않는 상담 아이디로 상담 삭제 시 예외 발생")
    void deleteFailWithInvalidId() {
        Long invalidId = 999L;
        Long agentId = agent.getId();

        when(consultationRepository.findById(invalidId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> consultationService.delete(invalidId, agentId));

        assertEquals("해당 상담 내역이 존재하지 않습니다:" + invalidId, exception.getMessage());
    }

    @Test
    @DisplayName("상담 수정 성공")
    void updateSuccess() {
        Long id = testConsultation.getId();
        Long agentId = agent.getId();

        given(consultationRepository.findByIdAndAgentAndDeletedAtIsNull(id, agent))
                .willReturn(Optional.of(testConsultation));

        ConsultationResDto result = consultationService.update(id, updateRequestDto, agentId);

        assertNotNull(result);
        assertEquals(updateRequestDto.getContent(), result.getContent());
    }

    @Test
    @DisplayName("존재하지 않는 상담 아이디로 상담 수정 시 예외 발생")
    void updateFailWithInvalidId() {
        Long invalidId = 999L;
        Long agentId = agent.getId();

        when(consultationRepository.findById(invalidId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> consultationService.update(invalidId, updateRequestDto, agentId));

        assertEquals("해당 상담 내역이 존재하지 않습니다:" + invalidId, exception.getMessage());
    }

    @Test
    @DisplayName("존재하지 않는 공인중개사로 상담 수정 시 예외 발생")
    void updateFailWithInvalidAgent() {
        Long id = testConsultation.getId();
        Long invalidAgentId = 999L;

        when(agentRepository.findById(invalidAgentId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> consultationService.update(id, updateRequestDto, invalidAgentId));

        assertEquals("공인중개사가 존재하지 않습니다.", exception.getMessage());
    }
}
