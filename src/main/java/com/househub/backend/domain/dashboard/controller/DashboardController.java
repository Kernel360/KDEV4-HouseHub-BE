package com.househub.backend.domain.dashboard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.common.util.SecurityUtil;
import com.househub.backend.domain.dashboard.dto.DashboardStatsResDto;
import com.househub.backend.domain.dashboard.service.DashboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
	private final DashboardService dashboardService;

	@Operation(
		summary = "대시보드 통계 조회",
		description = "대시보드 통계를 조회합니다. 에이전트 ID를 기반으로 통계를 가져옵니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "대시보드 통계 조회 성공"),
		@ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
		@ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
	})
	@GetMapping("/stats")
	public ResponseEntity<SuccessResponse<DashboardStatsResDto>> getDashboardStats() {
		DashboardStatsResDto response = dashboardService.getDashboardStats(getSignInAgentId());
		return ResponseEntity.ok(SuccessResponse.success("대시보드 통계 조회 성공", "DASHBOARD_STATS_SUCCESS", response));
	}

	/**
	 * 현재 로그인한 에이전트의 ID를 반환합니다.
	 *
	 * @return 현재 로그인한 에이전트의 ID
	 */
	private Long getSignInAgentId() {
		return SecurityUtil.getAuthenticatedAgent().getId();
	}

}
