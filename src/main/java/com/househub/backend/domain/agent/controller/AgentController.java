package com.househub.backend.domain.agent.controller;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.common.util.SecurityUtil;
import com.househub.backend.domain.agent.dto.AgentResDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/agents")
public class AgentController {

    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<AgentResDto>> getMyInfo() {
        log.info("내 정보 조회 API 호출");
        AgentResDto signInAgentInfo = SecurityUtil.getAuthenticatedAgent();

        return ResponseEntity.ok(SuccessResponse.success("내 정보 조회 성공", "GET_MY_INFO_SUCCESS", signInAgentInfo));
    }

    @GetMapping("")
    public String hello() {
        return "Hello, World!";
    }
}
