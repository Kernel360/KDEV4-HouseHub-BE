package com.househub.backend.domain.consultation.dto;

import com.househub.backend.domain.consultation.entity.Consultation;
import com.househub.backend.domain.consultation.enums.ConsultationStatus;
import com.househub.backend.domain.consultation.enums.ConsultationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ConsultationResDto {

    private Long agentId;
    private Long customerId;

    private ConsultationType consultationType; // PHONE, VISIT
    private String content;
    private LocalDateTime consultationDate;
    private ConsultationStatus status; // RESERVED, COMPLETED, CANCELED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static ConsultationResDto fromEntity(Consultation consultation) {
        return ConsultationResDto.builder()
                .agentId(consultation.getAgentId())
                .consultationType(consultation.getConsultationType())
                .content(consultation.getContent())
                .consultationDate(consultation.getConsultationDate())
                .status(consultation.getStatus())
                .createdAt(consultation.getCreatedAt())
                .updatedAt(consultation.getUpdatedAt())
                .deletedAt(consultation.getDeletedAt())
                .build();
    }
}
