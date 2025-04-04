package com.househub.backend.domain.consultation.entity;

import com.househub.backend.domain.consultation.dto.ConsultationReqDto;
import com.househub.backend.domain.consultation.dto.ConsultationResDto;
import com.househub.backend.domain.consultation.enums.ConsultationStatus;
import com.househub.backend.domain.consultation.enums.ConsultationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long agentId;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConsultationType consultationType; // PHONE, VISIT

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime consultationDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConsultationStatus status; // RESERVED, COMPLETED, CANCELLED

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @PrePersist
    public void onCreate() {
        if (this.status == null) {
            this.status = ConsultationStatus.RESERVED;
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void update(ConsultationReqDto consultationReqDto) {
        this.consultationType = consultationReqDto.getConsultationType();
        this.content = consultationReqDto.getContent();
        this.consultationDate = consultationReqDto.getConsultationDate();
        this.status = consultationReqDto.getStatus();
    }
}
