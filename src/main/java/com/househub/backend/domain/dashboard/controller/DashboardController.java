package com.househub.backend.domain.dashboard.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.common.util.SecurityUtil;
import com.househub.backend.domain.dashboard.dto.ChartDataResDto;
import com.househub.backend.domain.dashboard.dto.DashboardStatsResDto;
import com.househub.backend.domain.dashboard.dto.MultiDatasetChartResDto;
import com.househub.backend.domain.dashboard.dto.RecentPropertyResDto;
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

	@Operation(
		summary = "최근 등록된 매물 조회",
		description = "최근 등록된 매물을 조회합니다. 기본적으로 5개의 매물이 반환됩니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "최근 등록된 매물 조회 성공"),
		@ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
		@ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
	})
	@GetMapping("/properties/recent")
	public ResponseEntity<SuccessResponse<List<RecentPropertyResDto>>> getRecentProperties(
		@RequestParam(defaultValue = "5") int limit
	) {
		List<RecentPropertyResDto> recentProperties = dashboardService.getRecentProperties(getSignInAgentId(), limit);
		return ResponseEntity.ok(SuccessResponse.success("최근 등록된 매물 조회 성공", "RECENT_PROPERTIES_SUCCESS",
			recentProperties));
	}

	@Operation(
		summary = "매물 유형별 차트 데이터 조회",
		description = "매물 유형별 차트 데이터를 조회합니다. 에이전트 ID를 기반으로 매물 유형 차트를 가져옵니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "매물 유형별 차트 데이터 조회 성공"),
		@ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
		@ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
	})
	@GetMapping("/charts/properties")
	public ResponseEntity<SuccessResponse<ChartDataResDto>> getPropertyTypeChart() {
		ChartDataResDto chartData = dashboardService.getPropertyTypeChartData(getSignInAgentId());
		return ResponseEntity.ok(SuccessResponse.success("매물 유형별 차트 데이터 조회 성공", "PROPERTY_TYPE_CHART_SUCCESS",
			chartData));
	}

	@Operation(
		summary = "월별 계약 현황 차트 데이터 조회",
		description = "월별 계약 현황 차트 데이터를 조회합니다. 에이전트 ID를 기반으로 월별 계약 현황 차트를 가져옵니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "계약 유형 차트 조회 성공"),
		@ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
		@ApiResponse(responseCode = "403", description = "권한 없음", content = @Content)
	})
	@GetMapping("/charts/contracts")
	public ResponseEntity<SuccessResponse<MultiDatasetChartResDto>> getContractChart() {
		MultiDatasetChartResDto chartData = dashboardService.getContractChartData(getSignInAgentId());
		return ResponseEntity.ok(SuccessResponse.success("월별 계약 현황 차트 조회 성공", "CONTRACT_TYPE_CHART_SUCCESS",
			chartData));
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
