package com.househub.backend.domain.consultation.repository;

import com.househub.backend.domain.consultation.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
}
