package com.househub.backend.domain.dashboard.service;

<<<<<<< HEAD
import java.util.List;

import com.househub.backend.domain.dashboard.dto.ChartDataResDto;
import com.househub.backend.domain.dashboard.dto.DashboardStatsResDto;
import com.househub.backend.domain.dashboard.dto.MultiDatasetChartResDto;
import com.househub.backend.domain.dashboard.dto.RecentPropertyResDto;

public interface DashboardService {
	DashboardStatsResDto getDashboardStats(Long agentId);

	List<RecentPropertyResDto> getRecentProperties(Long agentId, int limit);

	ChartDataResDto getPropertyTypeChartData(Long agentId);

	MultiDatasetChartResDto getContractChartData(Long signInAgentId);
=======
import com.househub.backend.domain.dashboard.dto.DashboardStatsResDto;

public interface DashboardService {
	DashboardStatsResDto getDashboardStats(Long agentId);
>>>>>>> 21bde5a (byungchan, feature: 대시보드 통계 데이터 조회 API 구현 #101)
}
