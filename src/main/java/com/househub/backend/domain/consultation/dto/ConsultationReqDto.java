package com.househub.backend.domain.consultation.dto;

import java.time.LocalDateTime;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.consultation.entity.Consultation;
import com.househub.backend.domain.consultation.enums.ConsultationStatus;
import com.househub.backend.domain.consultation.enums.ConsultationType;
import com.househub.backend.domain.customer.entity.Customer;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
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

    public Consultation toEntity(Agent agent, Customer customer) {
        return Consultation.builder()
                .agent(agent)
                .customer(customer)
                .consultationType(this.getConsultationType())
                .content(this.getContent())
                .consultationDate(this.getConsultationDate())
                .status(this.getStatus())
                .build()
                ;
    }
}






