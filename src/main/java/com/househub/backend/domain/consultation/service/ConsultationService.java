package com.househub.backend.domain.consultation.service;

import com.househub.backend.domain.consultation.dto.ConsultationReqDto;
import com.househub.backend.domain.consultation.entity.Consultation;
import com.househub.backend.domain.consultation.repository.ConsultationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository consultationRepository;

    public Consultation create(
        ConsultationReqDto consultationReqDto
    ) {
        Consultation consultation = Consultation.builder()
                .agentId(consultationReqDto.getAgentId())
                .customerId(consultationReqDto.getCustomerId())
                .consultationType(consultationReqDto.getConsultationType())
                .content(consultationReqDto.getContent())
                .consultationDate(consultationReqDto.getConsultationDate())
                .status(consultationReqDto.getStatus())
                .build()
                ;

        return consultationRepository.save(consultation);
    }
}
