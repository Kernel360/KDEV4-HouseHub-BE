package com.househub.backend.domain.contract.dto;

import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.enums.ContractType;
import com.househub.backend.domain.property.entity.Property;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ContractReqDto {
    @NotNull
    private Long propertyId; // 매물 ID
    @NotNull
    private ContractType contractType; // 거래 유형 (매매, 전세, 월세)

    private Long salePrice; // 매매가 (매매 계약일 경우 필요)
    private Long jeonsePrice; // 전세가 (전세 계약일 경우 필요)
    private Integer monthlyRentFee; // 월세 금액 (월세 계약일 경우 필요)
    private Integer monthlyRentDeposit; // 월세 보증금 (월세 계약일 경우 필요)

    private String memo; // 참고 설명 (예: 계약 기간 등)

    // 자동 실행
    @AssertTrue(message = "거래 유형에 따라 적절한 가격 정보가 필요합니다.")
    public boolean isValidTransaction() {
        if (contractType == ContractType.SALE) { // 매매
            return salePrice != null && jeonsePrice == null && monthlyRentDeposit == null && monthlyRentFee == null;
        } else if (contractType == ContractType.JEONSE) { // 전세
            return salePrice == null && jeonsePrice != null && monthlyRentDeposit == null && monthlyRentFee == null;
        } else if (contractType == ContractType.MONTHLY_RENT) { // 월세
            return salePrice == null && jeonsePrice == null && monthlyRentDeposit != null && monthlyRentFee != null;
        }
        return false;
    }

    public Contract toEntity(Property property) {
        return Contract.builder()
                // 해당 매물 등록
                .property(property)
                .contractType(contractType)
                .salePrice(salePrice)
                .jeonsePrice(jeonsePrice)
                .monthlyRentFee(monthlyRentFee)
                .monthlyRentDeposit(monthlyRentDeposit)
                .status(ContractStatus.ON_SALE) // 기본값 설정
                .memo(memo)
                .build();
    }
}
