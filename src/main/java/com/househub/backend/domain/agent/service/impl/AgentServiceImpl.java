package com.househub.backend.domain.agent.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.domain.agent.dto.GetMyInfoResDto;
import com.househub.backend.domain.agent.dto.UpdateAgentReqDto;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.agent.service.AgentReader;
import com.househub.backend.domain.agent.service.AgentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {
	private final AgentReader agentReader;

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
		Agent agent = agentReader.findById(agentId);

		// 조회된 정보 토대로 GetMyInfoResDto 객체를 생성하여 반환한다.
		return GetMyInfoResDto.from(agent);
	}

	@Transactional
	@Override
	public GetMyInfoResDto updateMyInfo(UpdateAgentReqDto request, Long agentId) {

		Agent agent = agentReader.findById(agentId);
		// 이메일 중복 체크 (본인 이메일은 제외)
		if (request.getEmail() != null && !request.getEmail().isEmpty() && !agent.getEmail()
			.equals(request.getEmail())) {
			if (agentReader.existsByEmail(request.getEmail())) {
				throw new AlreadyExistsException("이미 사용 중인 이메일입니다.", "EMAIL_DUPLICATE");
			}
		}

		// 라이선스 번호 중복 체크 (본인 라이선스는 제외)
		if (request.getLicenseNumber() != null && !request.getLicenseNumber().isEmpty()
			&& (agent.getLicenseNumber() == null || !agent.getLicenseNumber().equals(request.getLicenseNumber()))) {
			if (agentReader.existsByLicenseNumber(request.getLicenseNumber())) {
				throw new AlreadyExistsException("이미 사용 중인 라이선스 번호입니다.", "LICENSE_DUPLICATE");
			}
		}
		agent.update(request);

		return GetMyInfoResDto.from(agent);
	}
}