package com.househub.backend.domain.dashboard.dto;

import java.time.Month;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MultiDatasetChartResDto {
	private String month;
	private int activeCount;
	private int completedCount;

	public static MultiDatasetChartResDto from(Month month, Map<Month, Long> activeMap, Map<Month, Long> completedMap) {
		return MultiDatasetChartResDto.builder()
			.month(month.getValue() + "월")
			.activeCount(activeMap.getOrDefault(month, 0L).intValue())
			.completedCount(completedMap.getOrDefault(month, 0L).intValue())
			.build();
	}
}
