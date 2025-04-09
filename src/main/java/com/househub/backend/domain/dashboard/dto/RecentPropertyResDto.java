package com.househub.backend.domain.dashboard.dto;

import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecentPropertyResDto {
	private Long id;
	private PropertyType propertyType;
	private String location;
	private String createdAt;

	public static RecentPropertyResDto fromEntity(Property p) {
		return RecentPropertyResDto.builder()
			.id(p.getId())
			.propertyType(p.getPropertyType())
			.location(p.getRoadAddress() + p.getDetailAddress())
			.createdAt(String.valueOf(p.getCreatedAt()))
			.build();
	}
}
