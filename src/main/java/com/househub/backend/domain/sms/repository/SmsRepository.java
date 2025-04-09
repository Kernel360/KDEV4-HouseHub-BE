package com.househub.backend.domain.sms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.sms.dto.SendSmsResDto;
import com.househub.backend.domain.sms.entity.Sms;
import com.househub.backend.domain.sms.entity.SmsTemplate;

public interface SmsRepository extends JpaRepository<Sms, Long> {

	List<Sms> getAllSmsByAgent(Agent agent);

	SendSmsResDto findByIdAndAgent(Long id, Agent agent);


	@Query(value = "SELECT t FROM SmsTemplate t " +
		"WHERE t.deletedAt IS NULL " +
		"AND t.agent.id = :agentId " +
		"AND (" +
		"   (:title IS NULL OR :content = '' OR t.title LIKE CONCAT('%', :title, '%')) OR " +
		"   (:content IS NULL OR :content = '' OR t.content LIKE CONCAT('%', :content, '%'))" +
		") " +
		"ORDER BY t.createdAt DESC")
	Page<SmsTemplate> findAllByAgentAndFiltersAndDeletedAtIsNull(@Param("agentId")Long id,@Param("title") String title,@Param("content") String content,Pageable pageable);


	@Query(value = "SELECT s FROM Sms s " +
		"WHERE s.deletedAt IS NULL " +
		"AND s.agent.id = :agentId " +
		"AND (" +
		"   (:receiver IS NULL OR :receiver = '' OR s.receiver LIKE CONCAT('%', :receiver, '%')) OR " +
		"   (:msg IS NULL OR :msg = '' OR s.msg LIKE CONCAT('%', :msg, '%'))" +
		") " +
		"ORDER BY s.createdAt DESC")
	Page<Sms> findAllSmsByAgentAndFiltersAndDeletedAtIsNull(@Param("agentId") Long id,@Param("receiver") String receiver,@Param("msg") String msg, Pageable pageable);
}
