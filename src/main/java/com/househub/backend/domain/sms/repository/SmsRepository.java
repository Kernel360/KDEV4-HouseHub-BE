package com.househub.backend.domain.sms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.househub.backend.domain.sms.dto.SendSmsResDto;
import com.househub.backend.domain.sms.entity.Sms;

public interface SmsRepository extends JpaRepository<Sms, Long> {

	SendSmsResDto findByIdAndAgentId(Long id, Long agentId);



	@Query(value = "SELECT s FROM Sms s " +
		"WHERE s.deletedAt IS NULL " +
		"AND s.agent.id = :agentId " +
		"AND (" +
		"   (:receiver IS NULL OR :receiver = '' OR s.receiver LIKE CONCAT('%', :receiver, '%')) OR " +
		"   (:msg IS NULL OR :msg = '' OR s.msg LIKE CONCAT('%', :msg, '%'))" +
		") " +
		"ORDER BY s.createdAt DESC")
	Page<Sms> findAllSmsByAgentIdAndFiltersAndDeletedAtIsNull(@Param("agentId") Long agentId,@Param("receiver") String receiver,@Param("msg") String msg, Pageable pageable);
}
