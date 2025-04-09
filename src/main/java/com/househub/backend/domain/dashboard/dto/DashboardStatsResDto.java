package com.househub.backend.domain.dashboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsResDto {
	private long totalProperties;
	private long activeContracts;
	private long newCustomers;
	private long completedContracts;
}
