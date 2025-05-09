package com.househub.backend.domain.property.dto;

import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PropertySummaryResDto {
    private Long id; // 매물 고유 식별자
    private PropertyType propertyType; // 매물 유형
    private String detailAddress; // 상세 주소
    private String roadAddress; // 전체 도로명 주소

    public static PropertySummaryResDto fromEntity(Property property) {
        return PropertySummaryResDto.builder()
                .id(property.getId())
                .propertyType(property.getPropertyType())
                .roadAddress(property.getRoadAddress())
                .detailAddress(property.getDetailAddress())
                .build();
    }
}
