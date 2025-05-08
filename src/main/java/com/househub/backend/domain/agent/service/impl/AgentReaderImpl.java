package com.househub.backend.domain.agent.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.enums.AgentStatus;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.agent.service.AgentReader;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AgentReaderImpl implements AgentReader {
	private final AgentRepository agentRepository;

	@Override
	public Agent findById(Long agentId) {
		// agentId 로 중개사 조회하는데, status 가 반드시 ACTIVE 인 중개사만 조회
		return agentRepository.findByIdAndStatus(agentId, AgentStatus.ACTIVE)
			.orElseThrow(() -> new ResourceNotFoundException("해당 중개사를 찾을 수 없습니다.", "AGENT_NOT_FOUND"));
	}

	@Override
	public boolean existsByEmail(String email) {
		return agentRepository.existsByEmail(email);
	}

	@Override
	public boolean existsByLicenseNumber(String licenseNumber) {
		return agentRepository.existsByLicenseNumber(licenseNumber);
	}

	@Override
	public boolean existsByContact(String contact) {
		return agentRepository.existsByContact(contact);
	}

	@Override
	public Agent findByContact(String contact) {
		return agentRepository.findByContact(contact).orElseThrow(() -> new ResourceNotFoundException("해당 중개사를 찾을 수 없습니다.", "AGENT_NOT_FOUND"));
	}

}
