package com.househub.backend.domain.property.dto;

import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyType;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FindPropertyResDto {

	private Long id; // 매물 고유 식별자
	private PropertyType propertyType; // 매물 유형
	private String detailAddress; // 상세 주소
	private String roadAddress; // 전체 도로명 주소

	// Entity -> DTO 변환
	public static FindPropertyResDto toDto(Property property) {
		return FindPropertyResDto.builder()
			.id(property.getId())
			.propertyType(property.getPropertyType())
			.detailAddress(property.getDetailAddress())
			.roadAddress(property.getRoadAddress())
			.build();
	}
}
