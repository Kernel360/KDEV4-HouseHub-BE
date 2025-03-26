package com.househub.backend.domain.consultation.dto;

import com.househub.backend.domain.consultation.entity.Consultation;
import com.househub.backend.domain.consultation.enums.ConsultationStatus;
import com.househub.backend.domain.consultation.enums.ConsultationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ConsultationReqDto {

    @NotNull(message = "공인중개사 id를 입력하세요.")
    private Long agentId;

    @NotNull(message = "고객 id를 입력하세요.")
    private Long customerId;

    @NotNull(message = "상담 수단을 입력하세요.")
    private ConsultationType consultationType; // PHONE, VISIT

    private String content;

    private LocalDateTime consultationDate;

    @NotNull(message = "상담 상태를 입력하세요.")
    private ConsultationStatus status; // RESERVED, COMPLETED, CANCELED

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public Consultation toEntity() {
        return Consultation.builder()
                .agentId(agentId)
                .customerId(customerId)
                .consultationType(this.getConsultationType())
                .content(this.getContent())
                .consultationDate(this.getConsultationDate())
                .status(this.getStatus())
                .build()
                ;
    }
}






