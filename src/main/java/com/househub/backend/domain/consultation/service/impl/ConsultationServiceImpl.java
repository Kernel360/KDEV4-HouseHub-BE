package com.househub.backend.domain.consultation.service.impl;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.consultation.dto.ConsultationReqDto;
import com.househub.backend.domain.consultation.dto.ConsultationResDto;
import com.househub.backend.domain.consultation.entity.Consultation;
import com.househub.backend.domain.consultation.repository.ConsultationRepository;
import com.househub.backend.domain.consultation.service.ConsultationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;

    @Transactional
    public ConsultationResDto create(
        ConsultationReqDto consultationReqDto
    ) {
        Consultation consultation = consultationReqDto.toEntity();
        return ConsultationResDto.fromEntity(consultationRepository.save(consultation));
    }

    public ConsultationResDto findOne(Long id) {
        Consultation consultation = consultationRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new ResourceNotFoundException("해당 상담 내역이 존재하지 않습니다:" + id, "CONSULTATION_NOT_FOUND"));
        return ConsultationResDto.fromEntity(consultation);
    }

    public List<ConsultationResDto> findAll() {
        List<Consultation> consultations = consultationRepository.findAllByDeletedAtIsNull();

        if (consultations.isEmpty()) {
            throw new ResourceNotFoundException("상담 내역이 존재하지 않습니다:", "CONSULTATION_NOT_FOUND");
        }

        return consultations.stream().map(ConsultationResDto::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public ConsultationResDto update(
        Long id,
        ConsultationReqDto consultationReqDto
    ) {
        Consultation consultation = consultationRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new ResourceNotFoundException("해당 상담 내역이 존재하지 않습니다:" + id, "CONSULTATION_NOT_FOUND"));

        consultation.update(consultationReqDto);

        return ConsultationResDto.fromEntity(consultation);
    }

    @Transactional
    public ConsultationResDto delete(Long id) {
        Consultation consultation = consultationRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new ResourceNotFoundException("해당 상담 내역이 존재하지 않습니다:" + id, "CONSULTATION_NOT_FOUND"));

        consultation.softDelete();

        return ConsultationResDto.fromEntity(consultation);
    }
}
