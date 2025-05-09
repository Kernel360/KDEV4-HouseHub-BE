package com.househub.backend.domain.property.dto;

import java.util.List;

import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.enums.ContractType;
import com.househub.backend.domain.customer.dto.CustomerResDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyType;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FindPropertyResDto {

	private Long id; // 매물 고유 식별자
//	private CustomerResDto customer;
	private PropertyType propertyType; // 매물 유형
	private String detailAddress; // 상세 주소
	private String roadAddress; // 전체 도로명 주소
	private String jibunAddress; // 지번 주소
	private Boolean active; // 매물이 계약 가능한지 여부 default : true (계약이 없는 경우 true)

	private List<ContractType> contractTypes; // 계약 유형 (전세, 월세, 매매 등)

	// Entity -> DTO 변환
	public static FindPropertyResDto fromEntity(Property property) {
		return FindPropertyResDto.builder()
			.id(property.getId())
//			.customer(CustomerResDto.fromEntity(property.getCustomer()))
			.propertyType(property.getPropertyType())
			.detailAddress(property.getDetailAddress())
			.roadAddress(property.getRoadAddress())
			.jibunAddress(property.getJibunAddress())
			.active(property.getActive())
				// 계약 가능 상태의 계약만 가져오기
			.contractTypes(property.getContracts().stream()
					.filter(c -> c.getStatus() == ContractStatus.AVAILABLE)
					.map(Contract::getContractType).toList())
			.build();
	}
}
