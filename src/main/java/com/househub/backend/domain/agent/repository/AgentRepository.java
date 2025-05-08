package com.househub.backend.domain.agent.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.enums.AgentStatus;

public interface AgentRepository extends JpaRepository<Agent, Long> {
	Optional<Agent> findByLicenseNumber(String licenseNumber);

	Optional<Agent> findByEmail(String email);

	Optional<Agent> findByIdAndStatus(Long agentId, AgentStatus status);

	boolean existsByIdAndStatus(Long agentId, AgentStatus status);

	boolean existsByEmail(String email);

	boolean existsByLicenseNumber(String licenseNumber);

	Optional<Agent> findByContact(String contact);
}
