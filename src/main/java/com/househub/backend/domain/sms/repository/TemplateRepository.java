package com.househub.backend.domain.sms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.sms.entity.SmsTemplate;

public interface TemplateRepository extends JpaRepository<SmsTemplate, Long> {

	List<SmsTemplate> findAllByAgentAndDeletedAtIsNull(Agent agent);

	Optional<SmsTemplate> findByIdAndAgentAndDeletedAtIsNull(Long id, Agent agent);
}
