package com.househub.backend.domain.property.dto;

import com.househub.backend.domain.contract.enums.ContractType;
import com.househub.backend.domain.property.enums.PropertyType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertySearchDto {
    private String province;      // 도, 특별시, 광역시
    private String city;          // 시/군/구
    private String dong;          // 읍/면/동
    private PropertyType propertyType;  // 매물 유형 (아파트, 오피스텔 등)
    private String agentName;     // 공인중개사 이름
    private String customerName;  // 고객 이름
    private Boolean active;         // 매물 활성화 여부

    private ContractType contractType; // 계약 유형 (매매, 전세, 월세)
    private Long minPrice;       // 최소 가격
    private Long maxPrice;       // 최대 가격
    private Long minDeposit;     // 최소 보증금
    private Long maxDeposit;     // 최대 보증금
    private Integer minMonthlyRent; // 최소 월세
    private Integer maxMonthlyRent; // 최대 월세

    private List<Long> tagIds;
}
