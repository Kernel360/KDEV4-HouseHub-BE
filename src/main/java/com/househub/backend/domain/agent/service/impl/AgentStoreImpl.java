package com.househub.backend.domain.agent.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.agent.service.AgentStore;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AgentStoreImpl implements AgentStore {
	private final AgentRepository agentRepository;

	@Override
	public void update(Agent agent) {
		agentRepository.save(agent);
	}
}
