package com.househub.backend.domain.inquiryTemplate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.Question;
import com.househub.backend.domain.inquiryTemplate.enums.InquiryType;

import org.springframework.data.repository.query.Param;  // Spring Data JPA의 @Param

@Repository
public interface InquiryTemplateRepository extends JpaRepository<InquiryTemplate, Long> {

	@Query("SELECT it FROM InquiryTemplate it " +
		"WHERE it.deletedAt IS NULL " +
		"AND it.id = :id " +
		"AND it.agent.id = :agentId")
	Optional<InquiryTemplate> findByIdAndAgentId(@Param("id") Long id, @Param("agentId") Long agentId);

	@Query("""
		SELECT it FROM InquiryTemplate it
		WHERE it.deletedAt IS NULL
		AND it.agent.id = :agentId
		AND (:#{#active == null} = true OR it.active = :active)
		AND (:#{#keyword == null or #keyword.isEmpty()} = true OR LOWER(it.name) LIKE %:keyword%)
		AND (:#{#type == null} = true OR it.type = :type)
		"""
	)
	Page<InquiryTemplate> findAllByAgentIdAndFilters(
		@Param("agentId") Long agentId,
		@Param("active") Boolean active,
		@Param("keyword") String keyword,
		@Param("type") InquiryType type,
		Pageable pageable
	);

	@Query("SELECT DISTINCT it FROM InquiryTemplate it " +
		"WHERE it.deletedAt IS NULL " +
		"AND it.agent.id = :agentId " +
		"AND (LOWER(it.name) LIKE CONCAT('%', :keyword, '%') OR LOWER(it.description) LIKE CONCAT('%', :keyword, '%'))")
	Page<InquiryTemplate> findAllByAgentIdAndKeyword(@Param("agentId") Long agentId,
		@Param("keyword") String keyword,
		Pageable pageable);

	@Query("SELECT CASE WHEN COUNT(it) > 0 THEN true ELSE false END " +
		"FROM InquiryTemplate it " +
		"WHERE it.agent.id = :agentId " +
		"AND it.name = :name " +
		"AND it.deletedAt IS NULL")
	boolean existsByAgentIdAndName(@Param("agentId") Long agentId, @Param("name") String name);

	@Query(
		"""
			SELECT DISTINCT it FROM InquiryTemplate it
			LEFT JOIN FETCH it.questions q
			WHERE it.deletedAt IS NULL
			AND it.id = :templateId
			AND it.agent.id = :agentId
			"""
	)
	Optional<InquiryTemplate> findWithQuestionsByIdAndAgentId(@Param("templateId") Long templateId,
		@Param("agentId") Long agentId);

	@Query(
		"""
			SELECT DISTINCT it FROM InquiryTemplate it
			LEFT JOIN FETCH it.questions q
			WHERE it.deletedAt IS NULL
			AND it.id = :templateId
			"""
	)
	Optional<InquiryTemplate> findActiveTemplateWithQuestionsById(@Param("templateId") Long templateId);

	@Query("SELECT it FROM InquiryTemplate it " +
		"LEFT JOIN FETCH it.questions q " +
		"WHERE it.id IN :ids")
	List<InquiryTemplate> findByIdsWithQuestions(@Param("ids") List<Long> ids);

	@Query("SELECT q FROM Question q " +
		"LEFT JOIN FETCH q.options o " +
		"WHERE q.inquiryTemplate.id IN :templateIds")
	List<Question> findQuestionsWithOptionsByTemplateIds(@Param("templateIds") List<Long> templateIds);
}
