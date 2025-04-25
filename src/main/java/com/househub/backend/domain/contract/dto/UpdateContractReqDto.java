package com.househub.backend.domain.contract.dto;

import java.time.LocalDate;

import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.enums.ContractType;

import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class UpdateContractReqDto {
	private Long propertyId; // 매물 ID
	private Long customerId; // 고객 ID
	private ContractType contractType; // 거래 유형 (매매, 전세, 월세)
	private ContractStatus contractStatus; // 거래 상태 ( 거래 가능, 진행중, 완료 )

	@Positive(message = "매매가는 0보다 큰 값이어야 합니다.")
	private Long salePrice; // 매매가 (매매 계약일 경우 필요)
	@Positive(message = "전세가는 0보다 큰 값이어야 합니다.")
	private Long jeonsePrice; // 전세가 (전세 계약일 경우 필요)
	@Positive(message = "월세는 0보다 큰 값이어야 합니다.")
	private Integer monthlyRentFee; // 월세 금액 (월세 계약일 경우 필요)
	@Positive(message = "보증금은 0보다 큰 값이어야 합니다.")
	private Integer monthlyRentDeposit; // 월세 보증금 (월세 계약일 경우 필요)

	private String memo; // 참고 설명 (예: 계약 기간 등)
	private LocalDate startedAt; // 계약 시작일 (매매일 경우 만료일과 동일)
	private LocalDate expiredAt; // 계약 만료일 (매매일 경우 시작일과 동일)

	private LocalDate completedAt; // 거래 완료일
	private Boolean active; // 활성화 여부


}
