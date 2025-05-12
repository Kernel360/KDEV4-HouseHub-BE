package com.househub.backend.domain.contract.dto;

import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.enums.ContractType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContractSummaryResDto {
    private Long id; // 계약 ID
    private ContractStatus status; // 계약 상태
    private ContractType contractType; // 거래 유형 (매매, 전세, 월세)
    private String salePrice; // 매매가
    private String jeonsePrice; // 전세가
    private String monthlyRentFee; // 월세 금액
    private String monthlyRentDeposit; // 월세 보증금

    public static ContractSummaryResDto fromEntity(Contract contract) {
        return ContractSummaryResDto.builder()
                .id(contract.getId())
                .status(contract.getStatus())
                .contractType(contract.getContractType())
                .salePrice(convertPriceFormat(contract.getSalePrice()))
                .jeonsePrice(convertPriceFormat(contract.getJeonsePrice()))
                .monthlyRentFee(convertPriceFormat(contract.getMonthlyRentFee()))
                .monthlyRentDeposit(convertPriceFormat(contract.getMonthlyRentDeposit()))
                .build();
    }

    private static String convertPriceFormat(Long price) {
        if (price == null) {
            return "-";
        }

        long eok = price / 100_000_000; // 억 단위
        long man = (price % 100_000_000) / 10_000; // 억을 제외한 나머지 만 단위

        StringBuilder sb = new StringBuilder();

        if (eok > 0) {
            sb.append(eok).append("억 ");
        }

        if (man > 0) {
            sb.append(String.format("%,d", man)).append("만");
        }

        if (sb.length() == 0) {
            // 억, 만 둘 다 0인 경우 (이론상 거의 없지만)
            return "0";
        }

        return sb.toString().trim();
    }
}
