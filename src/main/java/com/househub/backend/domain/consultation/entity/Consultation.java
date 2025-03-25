package com.househub.backend.domain.consultation.entity;

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
    public Long id;

    @Column(nullable = false, length = 50)
    private Long agentId;

    @Column(nullable = false, length = 50)
    private Long customerId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConsultationType consultationType; // PHONE, VISIT

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime consultationDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConsultationStatus status; // RESERVATED, COMPLETED, CANCELLED
}