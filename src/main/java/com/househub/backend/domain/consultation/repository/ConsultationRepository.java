package com.househub.backend.domain.consultation.repository;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.consultation.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    // deletedAt이 null인 데이터에 대한 조회
    Optional<Consultation> findByIdAndAgentAndDeletedAtIsNull(Long id, Agent agent);

    // deletedAt이 null인 데이터에 대한 조회
    List<Consultation> findAllByAgentAndDeletedAtIsNull(Agent agent);
}
