package com.househub.backend.domain.agent.repository;

import com.househub.backend.domain.agent.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findByLicenseNumber(String licenseNumber);
    Optional<Agent> findByEmail(String email);
}
