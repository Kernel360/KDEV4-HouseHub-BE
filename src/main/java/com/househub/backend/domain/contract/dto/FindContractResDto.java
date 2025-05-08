package com.househub.backend.domain.contract.dto;

import java.time.LocalDate;

import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.enums.ContractType;
import com.househub.backend.domain.customer.dto.CustomerSummaryResDto;

import com.househub.backend.domain.property.dto.PropertySummaryResDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindContractResDto {
    private Long id; // 계약 ID
//    private GetMyInfoResDto agent;
//    private FindPropertyResDto property; // 매물
//    private CustomerResDto customer; // 고객
    private ContractType contractType; // 거래 유형 (매매, 전세, 월세)
    private PropertySummaryResDto property;
    private CustomerSummaryResDto seeker; // 매수,임차
    private CustomerSummaryResDto provider; // 매도,임대

    private String salePrice; // 매매가
    private String jeonsePrice; // 전세가
    private String monthlyRentFee; // 월세 금액
    private String monthlyRentDeposit; // 월세 보증금

    private ContractStatus status; // 계약 상태
    private String memo; // 계약 관련 메모
    private LocalDate startedAt; // 계약 시작 일시
    private LocalDate expiredAt; // 계약 만료 일시
    private LocalDate completedAt; // 계약 완료 일시
    // private Boolean active; // 계약 활성화 여부

    // Contract 엔티티를 DTO로 변환
    public static FindContractResDto fromEntity(Contract contract) {
        return FindContractResDto.builder()
                .id(contract.getId())
//                .agent(GetMyInfoResDto.from(contract.getAgent()))
//                .property(FindPropertyResDto.toDto(contract.getProperty()))
//                .customer(contract.getCustomer() != null ?
//                        CustomerResDto.fromEntity(contract.getCustomer()) : null)
                .property(PropertySummaryResDto.fromEntity(contract.getProperty()))
                // 매수,임차인
                .seeker(contract.getCustomer() != null ?
                        CustomerSummaryResDto.fromEntity(contract.getCustomer()) : null)
                // 매도,임대인
                .provider(contract.getCustomer() != null ?
                        CustomerSummaryResDto.fromEntity(contract.getProperty().getCustomer()) : null)
                .contractType(contract.getContractType())
                .salePrice(convertPriceFormat(contract.getSalePrice()))
                .jeonsePrice(convertPriceFormat(contract.getJeonsePrice()))
                .monthlyRentFee(convertPriceFormat(contract.getMonthlyRentFee()))
                .monthlyRentDeposit(convertPriceFormat(contract.getMonthlyRentDeposit()))
                .status(contract.getStatus())
                .memo(contract.getMemo())
                .startedAt(contract.getStartedAt())
                .expiredAt(contract.getExpiredAt())
                .completedAt(contract.getCompletedAt())
                // .active(contract.getActive())
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
