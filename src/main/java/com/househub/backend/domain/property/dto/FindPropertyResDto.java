package com.househub.backend.domain.property.dto;

import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class FindPropertyResDto {

    private Long propertyId; // 매물 고유 식별자
    private PropertyType propertyType; // 매물 유형
    private String memo; // 참고 설명
    private String province; // 도, 특별시, 광역시
    private String city; // 시/군/구
    private String dong; // 읍/면/동
    private String detailAddress; // 상세 주소
    private String roadAddress; // 전체 도로명 주소
    private BigDecimal latitude; // 위도
    private BigDecimal longitude; // 경도

    // Entity -> DTO 변환
    public static FindPropertyResDto toDto(Property property) {
        return FindPropertyResDto.builder()
                .propertyId(property.getPropertyId())
                .propertyType(property.getPropertyType())
                .memo(property.getMemo())
                .province(property.getProvince())
                .city(property.getCity())
                .dong(property.getDong())
                .detailAddress(property.getDetailAddress())
                .roadAddress(property.getRoadAddress())
                .latitude(property.getLatitude())
                .longitude(property.getLongitude())
                .build();
    }
}
