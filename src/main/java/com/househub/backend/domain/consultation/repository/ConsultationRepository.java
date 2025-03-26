package com.househub.backend.domain.consultation.repository;

import com.househub.backend.domain.consultation.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    // deleteaAt이 null인 데이터에 대한 조회
    Optional<Consultation> findByIdAndDeletedAtIsNull(Long id);

    // deleteaAt이 null인 데이터에 대한 조회
    List<Consultation> findAllByDeletedAtIsNull();
}