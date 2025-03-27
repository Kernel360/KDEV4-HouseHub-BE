package com.househub.backend.domain.contract.dto;

import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.enums.ContractType;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.entity.Property;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ContractReqDto {
    @NotNull
    private Long propertyId; // 매물 ID
    @NotNull
    private Long customerId; // 고객 ID
    @NotNull
    private ContractType contractType; // 거래 유형 (매매, 전세, 월세)
    @NotNull
    private ContractStatus contractStatus; // 거래 상태 ( 판매중, 판매 완료 )

    private Long salePrice; // 매매가 (매매 계약일 경우 필요)
    private Long jeonsePrice; // 전세가 (전세 계약일 경우 필요)
    private Integer monthlyRentFee; // 월세 금액 (월세 계약일 경우 필요)
    private Integer monthlyRentDeposit; // 월세 보증금 (월세 계약일 경우 필요)

    private String memo; // 참고 설명 (예: 계약 기간 등)
    private LocalDate expiredAt; // 계약 만료일 (매매일 경우 불필요)

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

    // 매매 계약(SALE)일 경우 계약 만료일 불필요
    @AssertTrue(message = "매매 계약에서는 계약 만료일(expiredAt)을 입력할 필요가 없습니다.")
    public boolean isValidExpiredAt() {
        return !(contractType == ContractType.SALE && expiredAt != null);
    }

    public Contract toEntity(Property property, Customer customer) {
        return Contract.builder()
                .property(property)
                .customer(customer)
                .contractType(this.contractType)
                .salePrice(this.salePrice)
                .jeonsePrice(this.jeonsePrice)
                .monthlyRentFee(this.monthlyRentFee)
                .monthlyRentDeposit(this.monthlyRentDeposit)
                .status(this.contractStatus)
                .memo(this.memo)
                .build();
    }
}
