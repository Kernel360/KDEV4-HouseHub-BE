package com.househub.backend.domain.property.dto;

import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyType;
import com.househub.backend.domain.property.enums.TransactionType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreatePropertyReqDto {
    @NotNull
    private PropertyType propertyType;
    @NotNull
    private TransactionType transactionType;
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
                .propertyType(this.propertyType)
                .transactionType(this.transactionType)
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

    // 자동 실행
    @AssertTrue(message = "거래 유형에 따라 적절한 가격 정보가 필요합니다.")
    public boolean isValidTransaction() {
        if (transactionType == TransactionType.SALE) { // 매매
            return salePrice != null && jeonsePrice == null && monthlyRentDeposit == null && monthlyRentFee == null;
        } else if (transactionType == TransactionType.JEONSE) { // 전세
            return salePrice == null && jeonsePrice != null && monthlyRentDeposit == null && monthlyRentFee == null;
        } else if (transactionType == TransactionType.MONTHLY_RENT) { // 월세
            return salePrice == null && jeonsePrice == null && monthlyRentDeposit != null && monthlyRentFee != null;
        }
        return false;
    }
}
