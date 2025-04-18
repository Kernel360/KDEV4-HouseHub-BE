package com.househub.backend.domain.property.dto.propertyCondition;

import com.househub.backend.domain.contract.enums.ContractType;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.entity.PropertyCondition;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PropertyConditionReqDto {
	// 매물 등록할 때 같이 등록 시에는 null 허용, 추가 등록 시에는 필수
	// private Long propertyId; // 매물 ID
	@NotNull(message = "거래 유형을 입력해주세요.")
	private ContractType contractType; // 거래 유형 (매매, 전세, 월세)
	private Long salePrice; // 매매가
	private Long jeonsePrice; // 전세가
	private Integer monthlyRentFee; // 월세 금액
	private Integer monthlyRentDeposit; // 월세 보증금
	private String memo;
	private Boolean active; // 매물 조건 활성화 여부

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

	public PropertyCondition toEntity(Property property) {
		return PropertyCondition.builder()
			.property(property)
			.contractType(this.contractType)
			.salePrice(this.salePrice)
			.jeonsePrice(this.jeonsePrice)
			.monthlyRentFee(this.monthlyRentFee)
			.monthlyRentDeposit(this.monthlyRentDeposit)
			.memo(this.memo)
			.active(this.active != null ? this.active : true) // 기본값 true
			.build();
	}
}
