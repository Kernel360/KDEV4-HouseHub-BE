package com.househub.backend.domain.agent.service;

import com.househub.backend.domain.agent.dto.GetMyInfoResDto;
import com.househub.backend.domain.agent.dto.UpdateAgentReqDto;

public interface AgentService {
	GetMyInfoResDto getMyInfo(Long agentId);

	GetMyInfoResDto updateMyInfo(UpdateAgentReqDto request, Long agentId);
}
