package com.househub.backend.domain.dashboard.dto;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MultiDatasetChartResDto {
	private List<String> labels;
	private List<Dataset> datasets;

	public static MultiDatasetChartResDto from(
		Map<Month, Long> activeMap,
		Map<Month, Long> completedMap
	) {
		List<String> monthLabels = IntStream.rangeClosed(1, 12)
			.mapToObj(i -> Month.of(i).getDisplayName(TextStyle.SHORT, Locale.KOREAN))
			.collect(Collectors.toList());

		List<Long> activeData = IntStream.rangeClosed(1, 12)
			.mapToObj(i -> activeMap.getOrDefault(Month.of(i), 0L))
			.collect(Collectors.toList());

		List<Long> completedData = IntStream.rangeClosed(1, 12)
			.mapToObj(i -> completedMap.getOrDefault(Month.of(i), 0L))
			.collect(Collectors.toList());

		return new MultiDatasetChartResDto(
			monthLabels,
			List.of(
				new Dataset(
					"진행 중",
					activeData,
					"rgba(255, 205, 86, 0.5)", // yellow-like
					"rgba(255, 205, 86, 1)",
					1
				),
				new Dataset(
					"완료",
					completedData,
					"rgba(75, 192, 192, 0.5)", // green-blue
					"rgba(75, 192, 192, 1)",
					1
				)
			)
		);
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Dataset {
		private String label;
		private List<Long> data;
		private String backgroundColor;
		private String borderColor;
		private int borderWidth;
	}
}
