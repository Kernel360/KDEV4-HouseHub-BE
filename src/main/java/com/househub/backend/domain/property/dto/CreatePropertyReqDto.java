package com.househub.backend.domain.property.dto;

import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyType;
import com.househub.backend.domain.property.enums.TransactionType;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreatePropertyReqDto {
    private String propertyType;
    private String transactionType;
    private String memo;
    private String roadAddress; // 도로명 주소
    private String jibunAddress; // 지번 주소
    private String detailAddress; // 상세 주소

    private Long salePrice; // 매매가
    private Long jeonsePrice; // 전세 금액
    private Integer monthlyRentDeposit; // 월세 보증금
    private Integer monthlyRentFee; // 월세 금액

    private BigDecimal latitude;
    private BigDecimal longitude;

    public Property toEntity() {
        Property property = Property.builder()
                .propertyType(PropertyType.valueOf(this.propertyType))
                .transactionType(TransactionType.valueOf(this.transactionType))
                .memo(this.memo)
                .roadAddress(this.roadAddress)
                .detailAddress(this.detailAddress)
                .salePrice(this.salePrice)
                .jeonsePrice(this.jeonsePrice)
                .monthlyRentDeposit(this.monthlyRentDeposit)
                .monthlyRentFee(this.monthlyRentFee)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .build();
        property.parseJibunAddress(this.jibunAddress);
        return property;
    }
}
