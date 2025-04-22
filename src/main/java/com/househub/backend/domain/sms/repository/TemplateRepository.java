package com.househub.backend.domain.sms.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.househub.backend.domain.sms.entity.SmsTemplate;

public interface TemplateRepository extends JpaRepository<SmsTemplate, Long> {

	Optional<SmsTemplate> findByIdAndAgentIdAndDeletedAtIsNull(Long id, Long agentId);

	@Query(value = "SELECT t FROM SmsTemplate t " +
		"WHERE t.deletedAt IS NULL " +
		"AND t.agent.id = :agentId " +
		"AND (" +
		"   (:title IS NULL OR :content = '' OR t.title LIKE CONCAT('%', :title, '%')) OR " +
		"   (:content IS NULL OR :content = '' OR t.content LIKE CONCAT('%', :content, '%'))" +
		") " +
		"ORDER BY t.createdAt DESC")
	Page<SmsTemplate> findAllByAgentIdAndFiltersAndDeletedAtIsNull(@Param("agentId")Long agentId,@Param("title") String title,@Param("content") String content,
		Pageable pageable);
}
