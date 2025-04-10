package com.househub.backend.domain.consultation.dto;

import java.time.LocalDateTime;

import com.househub.backend.domain.consultation.entity.Consultation;
import com.househub.backend.domain.consultation.enums.ConsultationStatus;
import com.househub.backend.domain.consultation.enums.ConsultationType;
import com.househub.backend.domain.customer.entity.Customer;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConsultationResDto {

    private Long id;
    private Long agentId;
    private CustomerResDto customer;

    private ConsultationType consultationType; // PHONE, VISIT
    private String content;
    private LocalDateTime consultationDate;
    private ConsultationStatus status; // RESERVED, COMPLETED, CANCELED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static ConsultationResDto fromEntity(Consultation consultation) {
        return ConsultationResDto.builder()
            .id(consultation.getId())
            .agentId(consultation.getAgent().getId())
            .customer(CustomerResDto.fromEntity(consultation.getCustomer()))
            .consultationType(consultation.getConsultationType())
            .content(consultation.getContent())
            .consultationDate(consultation.getConsultationDate())
            .status(consultation.getStatus())
            .createdAt(consultation.getCreatedAt())
            .updatedAt(consultation.getUpdatedAt())
            .deletedAt(consultation.getDeletedAt())
            .build();
    }

    @Getter
    @Builder
    private static class CustomerResDto {
        private Long id;
        private String name;
        private String email;
        private String contact;

        public static CustomerResDto fromEntity(Customer customer) {
            return CustomerResDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .contact(customer.getContact())
                .build();
        }
    }
}
