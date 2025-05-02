package com.househub.backend.domain.agent.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.agent.dto.GetMyInfoResDto;
import com.househub.backend.domain.agent.dto.UpdateAgentReqDto;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.agent.service.AgentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {
	private final AgentRepository agentRepository;
	/**
	 * 세션에 저장된 agentId 로 내 정보 조회
	 * @param agentId 중개사 ID
	 * @return GetMyInfoResDto 객체 (내 정보 조회 응답 DTO)
	 * @throws IllegalArgumentException 중개사가 존재하지 않을 경우
	 */
	@Transactional(readOnly = true) // 트랜잭션 내에서 Lazy 로딩 가능
	@Override
	public GetMyInfoResDto getMyInfo(Long agentId) {
		// 400 응답 처리해야 하는 게 맞는 건가요?
		Agent agent = agentRepository.findById(agentId)
			.orElseThrow(() -> new ResourceNotFoundException("해당하는 중개사가 없습니다.", "AGENT_NOT_FOUND"));

		// 조회된 정보 토대로 GetMyInfoResDto 객체를 생성하여 반환한다.
		return GetMyInfoResDto.from(agent);
	}

	@Transactional
	@Override
	public GetMyInfoResDto updateMyInfo(UpdateAgentReqDto request, Long agentId) {

		Agent agent = agentRepository.findById(agentId).orElseThrow(() -> new ResourceNotFoundException("해당하는 중개사가 없습니다.", "AGENT_NOT_FOUND"));
		agent.update(request);

		return GetMyInfoResDto.from(agent);
	}
}