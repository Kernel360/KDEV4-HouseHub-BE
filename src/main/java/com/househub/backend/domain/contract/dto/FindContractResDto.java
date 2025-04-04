package com.househub.backend.domain.contract.dto;

import java.time.LocalDate;

import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.enums.ContractType;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;
import com.househub.backend.domain.property.dto.FindPropertyResDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindContractResDto {
    private Long id; // 계약 ID
    private FindPropertyResDto property; // 매물
    private CreateCustomerResDto customer; // 고객
    private ContractType contractType; // 거래 유형 (매매, 전세, 월세)
    private Long salePrice; // 매매가
    private Long jeonsePrice; // 전세가
    private Integer monthlyRentFee; // 월세 금액
    private Integer monthlyRentDeposit; // 월세 보증금
    private ContractStatus status; // 계약 상태 (ON_SALE, SOLD_OUT)
    private String memo; // 계약 관련 메모
    private LocalDate startedAt; // 계약 시작 일시
    private LocalDate expiredAt; // 계약 만료 일시

    // Contract 엔티티를 DTO로 변환
    public static FindContractResDto toDto(Contract contract) {
        return FindContractResDto.builder()
                .id(contract.getId())
                .property(FindPropertyResDto.toDto(contract.getProperty()))
                .customer(CreateCustomerResDto.fromEntity(contract.getCustomer()))
                .contractType(contract.getContractType())
                .salePrice(contract.getSalePrice())
                .jeonsePrice(contract.getJeonsePrice())
                .monthlyRentFee(contract.getMonthlyRentFee())
                .monthlyRentDeposit(contract.getMonthlyRentDeposit())
                .status(contract.getStatus())
                .memo(contract.getMemo())
                .startedAt(contract.getStartedAt())
                .expiredAt(contract.getExpiredAt())
                .build();
    }
}
