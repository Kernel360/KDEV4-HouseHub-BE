package com.househub.backend.domain.agent.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.common.util.SecurityUtil;
import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.agent.dto.GetMyInfoResDto;
import com.househub.backend.domain.agent.service.AgentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentController {
	private final AgentService agentService;

	@GetMapping("/me")
	public ResponseEntity<SuccessResponse<GetMyInfoResDto>> getMyInfo() {
		log.info("내 정보 조회 API 호출");
		AgentResDto signInAgentInfo = SecurityUtil.getAuthenticatedAgent();
		GetMyInfoResDto response = agentService.getMyInfo(signInAgentInfo.getId());

		return ResponseEntity.ok(SuccessResponse.success("내 정보 조회 성공", "GET_MY_INFO_SUCCESS", response));
	}

	@GetMapping("")
	public String hello() {
		return "Hello, World!";
	}
}
