package com.househub.backend.domain.property.dto;

import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyType;
import com.househub.backend.domain.property.enums.TransactionType;
import com.househub.backend.domain.property.enums.PropertyStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
public class FindPropertyResDto {

    private Long propertyId; // 매물 고유 식별자
    private PropertyType propertyType; // 매물 유형
    private TransactionType transactionType; // 거래 유형
    private String memo; // 참고 설명
    private String province; // 도, 특별시, 광역시
    private String city; // 시/군/구
    private String dong; // 읍/면/동
    private String detailAddress; // 상세 주소
    private String roadAddress; // 전체 도로명 주소
    private Long salePrice; // 매매가
    private Long jeonsePrice; // 전세 금액
    private Integer monthlyRentDeposit; // 월세 보증금
    private Integer monthlyRentFee; // 월세 금액
    private PropertyStatus status; // 상태 (판매 중, 판매 완료)
    private LocalDateTime createdAt; // 등록일시
    private LocalDateTime updatedAt; // 수정일시
    private BigDecimal latitude; // 위도
    private BigDecimal longitude; // 경도

    // Entity -> DTO 변환
    public static FindPropertyResDto toDto(Property property) {
        return FindPropertyResDto.builder()
                .propertyId(property.getPropertyId())
                .propertyType(property.getPropertyType())
                .transactionType(property.getTransactionType())
                .memo(property.getMemo())
                .province(property.getProvince())
                .city(property.getCity())
                .dong(property.getDong())
                .detailAddress(property.getDetailAddress())
                .roadAddress(property.getRoadAddress())
                .salePrice(property.getSalePrice())
                .jeonsePrice(property.getJeonsePrice())
                .monthlyRentDeposit(property.getMonthlyRentDeposit())
                .monthlyRentFee(property.getMonthlyRentFee())
                .status(property.getStatus())
                .createdAt(property.getCreatedAt())
                .updatedAt(property.getUpdatedAt())
                .latitude(property.getLatitude())
                .longitude(property.getLongitude())
                .build();
    }
}
