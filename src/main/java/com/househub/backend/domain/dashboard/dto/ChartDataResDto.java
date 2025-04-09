package com.househub.backend.domain.dashboard.dto;

import java.util.List;

import com.househub.backend.domain.property.enums.PropertyType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChartDataResDto {
	private List<PropertyType> labels;
	private List<Integer> data;
	private String title;

	public static ChartDataResDto from(List<PropertyType> labels, List<Integer> data, String title) {
		return ChartDataResDto.builder()
			.labels(labels)
			.data(data)
			.title(title)
			.build();
	}
}
