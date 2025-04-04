package com.househub.backend.domain.consultation.service;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.consultation.dto.ConsultationReqDto;
import com.househub.backend.domain.consultation.dto.ConsultationResDto;
import com.househub.backend.domain.consultation.entity.Consultation;
import com.househub.backend.domain.consultation.enums.ConsultationStatus;
import com.househub.backend.domain.consultation.enums.ConsultationType;
import com.househub.backend.domain.consultation.repository.ConsultationRepository;
import com.househub.backend.domain.consultation.service.impl.ConsultationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ConsultationServiceTest {
    @InjectMocks
    private ConsultationServiceImpl consultationService;

    @Mock
    private ConsultationRepository consultationRepository;

    private Consultation testConsultation;
    private ConsultationReqDto createRequestDto;
    private ConsultationReqDto updateRequestDto;
    private List<Consultation> testConsultations;

    @BeforeEach
    void setup() {
        testConsultation = Consultation.builder()
                .id(1L)
                .agentId(1L)
                .customerId(1L)
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
                Consultation.builder().id(2L).agentId(1L).customerId(2L)
                        .consultationType(ConsultationType.PHONE)
                        .content("자취생이 살만한 1억 이하의 아파트를 찾고 있습니다.")
                        .consultationDate(LocalDateTime.now())
                        .status(ConsultationStatus.RESERVED)
                        .build(),
                Consultation.builder().id(3L).agentId(1L).customerId(3L)
                        .consultationType(ConsultationType.VISIT)
                        .content("기혼부부가 살만한 10억 이하의 아파트를 찾고 있습니다.")
                        .consultationDate(LocalDateTime.now())
                        .status(ConsultationStatus.COMPLETED)
                        .build()
        );
    }

    @Test
    @DisplayName("상담 생성 성공")
    void createSuccess() {
        given(consultationRepository.save(any(Consultation.class)))
                .willReturn(testConsultation);

        ConsultationResDto result = consultationService.create(createRequestDto);

        assertNotNull(result);
        assertEquals(createRequestDto.getContent(), result.getContent());
    }

    @Test
    @DisplayName("상담 전체 조회 성공")
    void findAllSuccess() {
        given(consultationRepository.findAllByDeletedAtIsNull())
                .willReturn(testConsultations);

        List<ConsultationResDto> result = consultationService.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("상담 전체 조회 실패")
    void findAllFail() {
        given(consultationRepository.findAllByDeletedAtIsNull())
                .willReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> consultationService.findAll());
    }

    @Test
    @DisplayName("상담 조회 성공")
    void findOneSuccess() {
        given(consultationRepository.findByIdAndDeletedAtIsNull(1L))
                .willReturn(Optional.of(testConsultation));

        ConsultationResDto result = consultationService.findOne(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("상담 조회 실패")
    void findOneFail() {
        given(consultationRepository.findByIdAndDeletedAtIsNull(1L))
                .willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> consultationService.findOne(1L));
    }

    @Test
    @DisplayName("상담 삭제 성공")
    void deleteSuccess() {
        given(consultationRepository.findByIdAndDeletedAtIsNull(1L))
                .willReturn(Optional.of(testConsultation));

        ConsultationResDto result = consultationService.delete(1L);

        assertNotNull(result.getDeletedAt());
    }

    @Test
    @DisplayName("상담 삭제 실패")
    void deleteFail() {
        given(consultationRepository.findByIdAndDeletedAtIsNull(1L))
                .willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> consultationService.delete(1L));
    }

    @Test
    @DisplayName("상담 수정 성공")
    void updateSuccess() {
        given(consultationRepository.findByIdAndDeletedAtIsNull(1L))
                .willReturn(Optional.of(testConsultation));

        ConsultationResDto result = consultationService.update(1L, updateRequestDto);

        assertNotNull(result);
        assertEquals(updateRequestDto.getContent(), result.getContent());
    }

    @Test
    @DisplayName("상담 수정 실패")
    void updateFail_NotFound() {
        given(consultationRepository.findByIdAndDeletedAtIsNull(999L))
                .willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> consultationService.update(999L, updateRequestDto));
    }

}
