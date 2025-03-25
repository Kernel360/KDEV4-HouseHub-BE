package com.househub.backend.domain.consultation.dto;

import com.househub.backend.domain.consultation.entity.Consultation;
import com.househub.backend.domain.consultation.entity.ConsultationStatus;
import com.househub.backend.domain.consultation.entity.ConsultationType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ConsultationReqDto {

    @NotBlank
    private Long agentId;

    @NotBlank
    private Long customerId;

    @NotBlank
    private ConsultationType consultationType; // PHONE, VISIT

    @NotBlank
    private String content;

    @NotBlank
    private LocalDateTime consultationDate;

    @NotBlank
    private ConsultationStatus status; // RESERVATED, COMPLETED, CANCELLED
}