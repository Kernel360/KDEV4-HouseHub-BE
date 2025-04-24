package com.househub.backend.domain.consultation.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.consultation.entity.Consultation;
import com.househub.backend.domain.consultation.enums.ConsultationStatus;
import com.househub.backend.domain.consultation.enums.ConsultationType;

import io.lettuce.core.dynamic.annotation.Param;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

	// deletedAt이 null인 데이터에 대한 조회
	Optional<Consultation> findByIdAndAgentAndDeletedAtIsNull(Long id, Agent agent);

	// deletedAt이 null인 데이터에 대한 조회
	List<Consultation> findAllByAgentAndDeletedAtIsNull(Agent agent);

	@Query("""
		    SELECT c FROM Consultation c
		    WHERE c.agent.id = :agentId
		    AND c.deletedAt IS NULL
		    AND (:keyword IS NULL OR LOWER(c.customer.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
		                      OR LOWER(c.content) LIKE LOWER(CONCAT('%', :keyword, '%')))
		    AND (:startDate IS NULL OR c.consultationDate >= :startDate)
		    AND (:endDate IS NULL OR c.consultationDate <= :endDate)
		    AND (:type IS NULL OR c.consultationType = :type)
		    AND (:status IS NULL OR c.status = :status)
		""")
	Page<Consultation> searchConsultations(
		@Param("agentId") Long agentId,
		@Param("keyword") String keyword,
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate,
		@Param("type") ConsultationType type,
		@Param("status") ConsultationStatus status,
		Pageable pageable
	);

	@Query("""
		    SELECT c FROM Consultation c
		    WHERE c.agent.id = :agentId
		    AND c.deletedAt IS NULL
		    AND (:customerName IS NULL OR LOWER(c.customer.name) LIKE LOWER(CONCAT('%', :customerName, '%')))
		""")
	Page<Consultation> searchConsultationsByCustomerName(
		@Param("agentId") Long agentId,
		@Param("customerName") String customerName,
		Pageable pageable
	);

	// ConsultationRepository
	@Query("SELECT c FROM Consultation c WHERE c.customer.id = :customerId AND c.agent.id = :agentId")
	List<Consultation> findAllByCustomerIds(@Param("customerId") Long customerId, @Param("agentId") Long agentId);

	Long agent(Agent agent);
}
