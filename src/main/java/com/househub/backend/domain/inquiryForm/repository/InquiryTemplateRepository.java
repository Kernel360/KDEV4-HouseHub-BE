package com.househub.backend.domain.inquiryForm.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.inquiryForm.entity.InquiryTemplate;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface InquiryTemplateRepository extends JpaRepository<InquiryTemplate, Long> {

	@Query("SELECT it FROM InquiryTemplate it " +
		"WHERE it.deletedAt IS NULL " +
		"AND it.id = :id " +
		"AND it.agent.id = :agentId")
	Optional<InquiryTemplate> findByIdAndAgentId(Long id, @Param("agentId") Long agentId);

	@Query("SELECT it FROM InquiryTemplate it " +
		"WHERE it.deletedAt IS NULL " +
		"AND it.agent.id = :agentId " +
		"AND (:isActive IS NULL OR it.isActive = :isActive)")
	Page<InquiryTemplate> findAllByAgentIdAndFilters(@Param("agentId") Long agentId,
		@Param("isActive") Boolean isActive, Pageable pageable);

	@Query("SELECT it FROM InquiryTemplate it " +
		"WHERE it.deletedAt IS NULL " +
		"AND (:isActive IS NULL OR it.isActive = :isActive)")
	Page<InquiryTemplate> findAllByFilters(@Param("isActive") Boolean isActive, Pageable pageable);

	@Query("SELECT it FROM InquiryTemplate it " +
		"WHERE it.deletedAt IS NULL " +
		"AND it.agent.id = :agentId " +
		"AND (it.name LIKE %:keyword% OR it.description LIKE %:keyword%)")
	Page<InquiryTemplate> findAllByAgentIdAndKeyword(@Param("agentId") Long agentId, @Param("keyword") String keyword,
		Pageable pageable);

	@Query("SELECT it FROM InquiryTemplate it " +
		"WHERE it.deletedAt IS NULL " +
		"AND (it.name LIKE %:keyword% OR it.description LIKE %:keyword%)")
	Page<InquiryTemplate> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

	@Query("SELECT it FROM InquiryTemplate it " +
		"WHERE it.deletedAt IS NULL " +
		"AND it.name = :name " +
		"AND it.agent.id = :agentId")
	boolean existsByNameAndAgentId(String name, @Param("agentId") Long agentId);
}
