package com.househub.backend.domain.agent.service;

import com.househub.backend.domain.agent.entity.Agent;

public interface AgentReader {
	Agent findById(Long agentId);

	boolean existsByEmail(String email);

	boolean existsByLicenseNumber(String licenseNumber);

	boolean existsByContact(String contact);

	Agent findByContact(String contact);

	Agent findByEmail(String email);
}
