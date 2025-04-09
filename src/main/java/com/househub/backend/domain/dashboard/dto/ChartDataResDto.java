package com.househub.backend.domain.dashboard.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.househub.backend.domain.property.enums.PropertyType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChartDataResDto {
	private List<String> labels;
	private List<Dataset> datasets;

	public static ChartDataResDto from(List<PropertyType> labels, List<Integer> data, String label) {
		List<String> labelStrings = labels.stream()
			.map(PropertyType::getKoreanName)
			.collect(Collectors.toList());

		List<String> backgroundColors = labels.stream()
			.map(l -> BACKGROUND_COLORS.getOrDefault(l.name(), "rgba(201, 203, 207, 0.2)"))
			.collect(Collectors.toList());

		List<String> borderColors = labels.stream()
			.map(l -> BORDER_COLORS.getOrDefault(l.name(), "rgba(201, 203, 207, 1)"))
			.collect(Collectors.toList());

		Dataset dataset = new Dataset(label, data, backgroundColors, borderColors, 1);

		return new ChartDataResDto(labelStrings, List.of(dataset));
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Dataset {
		private String label;
		private List<Integer> data;
		private List<String> backgroundColor;
		private List<String> borderColor;
		private int borderWidth = 1;
	}

	private static final Map<String, String> BACKGROUND_COLORS = Map.of(
		"APARTMENT", "rgba(255, 99, 132, 0.2)",
		"VILLA", "rgba(54, 162, 235, 0.2)",
		"COMMERCIAL", "rgba(255, 206, 86, 0.2)",
		"ONE_ROOM", "rgba(75, 192, 192, 0.2)",
		"TWO_ROOM", "rgba(153, 102, 255, 0.2)",
		"OFFICETEL", "rgba(255, 159, 64, 0.2)"
	);

	private static final Map<String, String> BORDER_COLORS = Map.of(
		"APARTMENT", "rgba(255, 99, 132, 1)",
		"VILLA", "rgba(54, 162, 235, 1)",
		"COMMERCIAL", "rgba(255, 206, 86, 1)",
		"ONE_ROOM", "rgba(75, 192, 192, 1)",
		"TWO_ROOM", "rgba(153, 102, 255, 1)",
		"OFFICETEL", "rgba(255, 159, 64, 1)"
	);
}