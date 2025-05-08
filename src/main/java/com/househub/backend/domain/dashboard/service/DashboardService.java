package com.househub.backend.domain.dashboard.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.contract.dto.ExpiringContractListResDto;
import com.househub.backend.domain.dashboard.dto.ChartDataResDto;
import com.househub.backend.domain.dashboard.dto.DashboardStatsResDto;
import com.househub.backend.domain.dashboard.dto.MultiDatasetChartResDto;
import com.househub.backend.domain.dashboard.dto.RecentPropertyResDto;

public interface DashboardService {
	DashboardStatsResDto getDashboardStats(Long agentId);

	List<RecentPropertyResDto> getRecentProperties(Long agentId, int limit);

	ChartDataResDto getPropertyTypeChartData(Long agentId);

	MultiDatasetChartResDto getContractChartData(Long signInAgentId);

	ExpiringContractListResDto getExpiringContracts(String yearMonth, AgentResDto agentDto, Pageable pageable);
}
