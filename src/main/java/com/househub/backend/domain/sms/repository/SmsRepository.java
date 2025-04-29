package com.househub.backend.domain.sms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.househub.backend.domain.sms.entity.Sms;
import com.househub.backend.domain.sms.enums.SmsStatus;

public interface SmsRepository extends JpaRepository<Sms, Long> {

	Sms findByIdAndAgentId(Long id, Long agentId);



	@Query(value = "SELECT s FROM Sms s " +
		"WHERE s.deletedAt IS NULL " +
		"AND s.agent.id = :agentId " +
		"AND (" +
		"   (:receiver IS NULL OR :receiver = '' OR " +
		"       REPLACE(REPLACE(s.receiver, '-', ''), ' ', '') " +
		"       LIKE CONCAT('%', REPLACE(REPLACE(:receiver, '-', ''), ' ', ''), '%')) OR " +
		"   (:msg IS NULL OR :msg = '' OR s.msg LIKE CONCAT('%', :msg, '%'))" +
		") " +
		"AND (:templateId IS NULL OR s.smsTemplate.id = :templateId) "+
		"ORDER BY s.createdAt DESC")
	Page<Sms> findAllSmsByAgentIdAndFiltersAndDeletedAtIsNull(@Param("agentId") Long agentId,@Param("receiver") String receiver,@Param("msg") String msg, @Param("templateId")Long templateId, Pageable pageable);

	List<Sms> findSmsByStatus(SmsStatus status);
}
