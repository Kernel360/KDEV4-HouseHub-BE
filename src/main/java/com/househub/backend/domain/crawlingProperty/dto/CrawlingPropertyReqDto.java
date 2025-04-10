package com.househub.backend.domain.crawlingProperty.dto;

import com.househub.backend.domain.crawlingProperty.enums.PropertyType;
import com.househub.backend.domain.crawlingProperty.enums.TransactionType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CrawlingPropertyReqDto {

    private PropertyType propertyType;

    private TransactionType transactionType;

    private String province;

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
