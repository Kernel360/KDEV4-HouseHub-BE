package com.househub.backend.domain.contract.dto;

import java.time.LocalDate;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.enums.ContractType;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.entity.Property;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateContractReqDto {
    @NotNull(message = "매물 id를 입력해 주세요.")
    private Long propertyId; // 매물 ID
    private Long customerId; // 고객 ID
    @NotNull(message = "거래 유형은 필수입니다.")
    private ContractType contractType; // 거래 유형 (매매, 전세, 월세)
    @NotNull(message = "거래 상태는 필수입니다.")
    private ContractStatus contractStatus; // 거래 상태 ( 거래 가능, 진행중, 완료 )

    @Positive(message = "매매가는 0보다 큰 값이어야 합니다.")
    private Long salePrice; // 매매가 (매매 계약일 경우 필요)
    @Positive(message = "전세가는 0보다 큰 값이어야 합니다.")
    private Long jeonsePrice; // 전세가 (전세 계약일 경우 필요)
    @Positive(message = "월세는 0보다 큰 값이어야 합니다.")
    private Long monthlyRentFee; // 월세 금액 (월세 계약일 경우 필요)
    @Positive(message = "보증금은 0보다 큰 값이어야 합니다.")
    private Long monthlyRentDeposit; // 월세 보증금 (월세 계약일 경우 필요)

    private String memo; // 참고 설명 (예: 계약 기간 등)
    private LocalDate startedAt; // 계약 시작일 (매매일 경우 만료일과 동일)
    private LocalDate expiredAt; // 계약 만료일 (매매일 경우 시작일과 동일)

    private LocalDate completedAt; // 거래 완료일
    // private Boolean active; // 계약 활성화 여부

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

    @AssertTrue(message = "거래 완료 상태일 경우, 거래 완료일은 필수입니다.")
    public boolean isCompletedAtRequired() {
        if (contractStatus == ContractStatus.COMPLETED) {
            return completedAt != null;
        }
        return true; // 거래 완료 상태가 아니면 통과
    }

    @AssertTrue(message = "계약 시작일은 계약 만료일보다 이후일 수 없습니다.")
    public boolean isValidContractPeriod() {
        if (startedAt == null || expiredAt == null) {
            return true;
        }
        return !startedAt.isAfter(expiredAt);
    }

    @AssertTrue(message = "거래 가능 상태일 경우, 거래 시작일과 만료일은 입력할 수 없습니다.")
    public boolean isValidContractStatus() {
        if (contractStatus == ContractStatus.AVAILABLE) { // 거래가능일 경우
            return startedAt == null && expiredAt == null;
        }
        return true;
    }

    @AssertTrue(message = "의뢰인의 존재 여부에 따라 계약 상태가 일관되어야 합니다.")
    public boolean isCustomerStatusConsistent() {
        if (customerId == null) {
            return contractStatus == ContractStatus.AVAILABLE;
        }
        return contractStatus != ContractStatus.AVAILABLE;
    }

    public Contract toEntity(Property property, Customer customer, Agent agent) {
        return Contract.builder()
                .property(property)
                .customer(customer)
                .agent(agent)
                .contractType(this.contractType)
                .salePrice(this.salePrice)
                .jeonsePrice(this.jeonsePrice)
                .monthlyRentFee(this.monthlyRentFee)
                .monthlyRentDeposit(this.monthlyRentDeposit)
                .status(this.contractStatus)
                .memo(this.memo)
                .startedAt(this.startedAt)
                .expiredAt(this.expiredAt)
                .completedAt(this.completedAt)
                .build();
    }
}
