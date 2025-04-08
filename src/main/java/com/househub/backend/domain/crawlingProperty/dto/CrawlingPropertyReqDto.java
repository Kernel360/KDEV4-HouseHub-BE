package com.househub.backend.domain.crawlingProperty.dto;

import com.househub.backend.domain.crawlingProperty.enums.PropertyType;
import com.househub.backend.domain.crawlingProperty.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CrawlingPropertyReqDto {

    @NotNull(message = "매물 종류를 선택하세요.")
    private PropertyType propertyType;

    @NotNull(message = "거래 유형을 선택하세요.")
    private TransactionType transactionType;

    @NotNull(message = " 도/특별시/광역시 입력하세요.")
    private String province;

    @NotNull(message = " 시/군/구를 입력하세요.")
    private String city;

    private String dong;

    private String detailAddress;

    private Float minArea;

    private Float maxArea;

    private Float minSalePrice;

    private Float maxSalePrice;

    private Float minDeposit;

    private Float maxDeposit;

    private Float minMonthlyRent;

    private Float maxMonthlyRent;

    public Object[] searchArgs() {
        return new Object[] {
            this.propertyType,
            this.transactionType,
            this.province,
            this.city,
            this.dong,
            this.detailAddress,
            this.minArea,
            this.maxArea,
            this.minSalePrice,
            this.maxSalePrice,
            this.minDeposit,
            this.maxDeposit,
            this.minMonthlyRent,
            this.maxMonthlyRent
        };
    }
}
