package com.househub.backend.domain.dashboard.service;

import com.househub.backend.domain.dashboard.dto.DashboardStatsResDto;

public interface DashboardService {
	DashboardStatsResDto getDashboardStats(Long agentId);
}
