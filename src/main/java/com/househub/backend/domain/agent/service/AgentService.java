package com.househub.backend.domain.agent.service;

import com.househub.backend.domain.agent.dto.GetMyInfoResDto;

public interface AgentService {
	GetMyInfoResDto getMyInfo(Long agentId);
}
