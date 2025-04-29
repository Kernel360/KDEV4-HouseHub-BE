package com.househub.backend.domain.contract.dto;

import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.enums.ContractType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractSearchDto {
	private String agentName;     // 공인중개사 이름
	private String customerName;  // 고객 이름
	private ContractType contractType;  // 거래 유형 (매매, 전세, 월세)
	private ContractStatus status;     // 계약 상태 (거래 가능, 거래 중, 거래 완료)

	private Boolean active;         // 계약 활성화 여부
}
