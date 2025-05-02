package com.househub.backend.domain.contract.dto;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.enums.ContractType;
import com.househub.backend.domain.property.entity.Property;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class BasicContractDto {
    @NotNull(message = "거래 유형은 필수입니다.")
    private ContractType contractType; // 거래 유형 (매매, 전세, 월세)
    @Positive(message = "매매가는 0보다 큰 값이어야 합니다.")
    private Long salePrice; // 매매가 (매매 계약일 경우 필요)
    @Positive(message = "전세가는 0보다 큰 값이어야 합니다.")
    private Long jeonsePrice; // 전세가 (전세 계약일 경우 필요)
    @Positive(message = "월세는 0보다 큰 값이어야 합니다.")
    private Long monthlyRentFee; // 월세 금액 (월세 계약일 경우 필요)
    @Positive(message = "보증금은 0보다 큰 값이어야 합니다.")
    private Long monthlyRentDeposit; // 월세 보증금 (월세 계약일 경우 필요)

    // 자동 실행
    @AssertTrue(message = "거래 유형에 따라 적절한 가격 정보가 필요합니다.")
    public boolean isValidContractType() {
        if (contractType == ContractType.SALE) { // 매매
            return salePrice != null && jeonsePrice == null && monthlyRentDeposit == null && monthlyRentFee == null;
        } else if (contractType == ContractType.JEONSE) { // 전세
            return salePrice == null && jeonsePrice != null && monthlyRentDeposit == null && monthlyRentFee == null;
        } else if (contractType == ContractType.MONTHLY_RENT) { // 월세
            return salePrice == null && jeonsePrice == null && monthlyRentDeposit != null && monthlyRentFee != null;
        }
        return false;
    }

    public Contract toEntity(Property property, Agent agent) {
        return Contract.builder()
                .property(property)
                .agent(agent)
                .status(ContractStatus.AVAILABLE)
                .contractType(this.contractType)
                .salePrice(this.salePrice)
                .jeonsePrice(this.jeonsePrice)
                .monthlyRentFee(this.monthlyRentFee)
                .monthlyRentDeposit(this.monthlyRentDeposit)
                .build();
    }
}
