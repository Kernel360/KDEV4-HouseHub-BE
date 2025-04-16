package com.househub.backend.domain.property.dto.propertyCondition;

import com.househub.backend.domain.contract.enums.ContractType;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;

@Getter
public class PropertyConditionUpdateReqDto {
	private ContractType contractType; // 거래 유형 (매매, 전세, 월세)
	private Long salePrice; // 매매가
	private Long jeonsePrice; // 전세가
	private Integer monthlyRentFee; // 월세 금액
	private Integer monthlyRentDeposit; // 월세 보증금
	private String memo;
	private Boolean active; // 매물 조건 활성화 여부

	@AssertTrue(message = "거래 유형에 따라 적절한 가격 정보가 필요합니다.")
	public boolean isValidContractType() {
		if(contractType == null) {
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
}

