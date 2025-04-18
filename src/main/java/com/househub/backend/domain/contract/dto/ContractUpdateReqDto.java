package com.househub.backend.domain.contract.dto;

import java.time.LocalDate;

import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.enums.ContractType;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;

@Getter
public class ContractUpdateReqDto {
	// private Long propertyConditionId; // 매물 조건 ID
	private Long customerId; // 고객 ID
	private ContractType contractType; // 거래 유형 (매매, 전세, 월세)
	private ContractStatus contractStatus; // 거래 상태 ( 거래 가능, 진행중, 완료 )

	private Long salePrice; // 매매가 (매매 계약일 경우 필요)
	private Long jeonsePrice; // 전세가 (전세 계약일 경우 필요)
	private Integer monthlyRentFee; // 월세 금액 (월세 계약일 경우 필요)
	private Integer monthlyRentDeposit; // 월세 보증금 (월세 계약일 경우 필요)

	private String memo; // 참고 설명 (예: 계약 기간 등)
	private LocalDate startedAt; // 계약 시작일 (매매일 경우 만료일과 동일)
	private LocalDate expiredAt; // 계약 만료일 (매매일 경우 시작일과 동일)

	private LocalDate completedAt; // 거래 완료일

	@AssertTrue(message = "거래 유형에 따라 적절한 가격 정보가 필요합니다.")
	public boolean isValidContractType() {
		if (contractType == null) {
			return true; // 수정 시에는 contractType이 null이어도 허용
		}
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
		return true; // 거래 완료 상태가 아니면 통과, null이어도 통과
	}

	@AssertTrue(message = "계약 시작일과 만료일은 함께 입력되어야 하며, 시작일은 만료일보다 이후일 수 없습니다.")
	public boolean isValidContractPeriod() {
		// 둘 다 없으면 OK
		if (startedAt == null && expiredAt == null) {
			return true;
		}
		// 하나만 있으면 x
		if (startedAt == null || expiredAt == null) {
			return false;
		}
		// 둘 다 있으면 비교
		return !startedAt.isAfter(expiredAt);
	}
}
