package com.househub.backend.domain.property.dto.propertyCondition;

import com.househub.backend.domain.contract.enums.ContractType;
import com.househub.backend.domain.property.entity.PropertyCondition;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PropertyConditionResDto {
	private Long id; // 매물 조건 ID
	private ContractType contractType; // 거래 유형 (매매, 전세, 월세)
	private Long salePrice; // 매매가
	private Long jeonsePrice; // 전세가
	private Integer monthlyRentFee; // 월세 금액
	private Integer monthlyRentDeposit; // 월세 보증금
	private String memo; // 계약 관련 메모
	private Boolean active; // 매물 조건 활성화 여부

	public static PropertyConditionResDto fromEntity(PropertyCondition condition) {
		return PropertyConditionResDto.builder()
			.id(condition.getId())
			.contractType(condition.getContractType())
			.salePrice(condition.getSalePrice())
			.jeonsePrice(condition.getJeonsePrice())
			.monthlyRentFee(condition.getMonthlyRentFee())
			.monthlyRentDeposit(condition.getMonthlyRentDeposit())
			.memo(condition.getMemo())
			.active(condition.getActive())
			.build();
	}
}
