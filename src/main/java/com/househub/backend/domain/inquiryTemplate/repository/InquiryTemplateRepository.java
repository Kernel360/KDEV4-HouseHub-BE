package com.househub.backend.domain.inquiryTemplate.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface InquiryTemplateRepository extends JpaRepository<InquiryTemplate, Long> {

	@Query("SELECT it FROM InquiryTemplate it " +
		"WHERE it.deletedAt IS NULL " +
		"AND it.id = :id " +
		"AND it.agent.id = :agentId")
	Optional<InquiryTemplate> findByIdAndAgentId(@Param("id") Long id, @Param("agentId") Long agentId);

	@Query("SELECT it FROM InquiryTemplate it " +
		"WHERE it.deletedAt IS NULL " +
		"AND it.agent.id = :agentId " +
		"AND (:#{#active == null} = true OR it.active = :active) " +
		"AND (:#{#keyword == null or #keyword.isEmpty()} = true OR LOWER(it.name) LIKE %:keyword%)")
	Page<InquiryTemplate> findAllByAgentIdAndFilters(@Param("agentId") Long agentId,
		@Param("active") Boolean active,
		@Param("keyword") String keyword,
		Pageable pageable);

	@Query("SELECT it FROM InquiryTemplate it " +
		"WHERE it.deletedAt IS NULL " +
		"AND it.agent.id = :agentId " +
		"AND (it.name LIKE %:keyword% OR it.description LIKE %:keyword%)")
	Page<InquiryTemplate> findAllByAgentIdAndKeyword(@Param("agentId") Long agentId,
		@Param("keyword") String keyword,
		Pageable pageable);

	@Query("SELECT CASE WHEN COUNT(it) > 0 THEN true ELSE false END " +
		"FROM InquiryTemplate it " +
		"WHERE it.agent.id = :agentId " +
		"AND it.name = :name " +
		"AND it.deletedAt IS NULL")
	boolean existsByAgentIdAndName(@Param("agentId") Long agentId, @Param("name") String name);
}
