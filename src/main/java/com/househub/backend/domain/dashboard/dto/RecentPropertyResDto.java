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
<<<<<<< HEAD
			.location(p.getRoadAddress() + " " + p.getDetailAddress())
=======
			.location(p.getRoadAddress() + p.getDetailAddress())
>>>>>>> e670440 (byungchan, feature: 최근 등록된 매물 목록 조회 API 구현 #101)
			.createdAt(String.valueOf(p.getCreatedAt()))
			.build();
	}
}
