package com.househub.backend.domain.property.dto;

import com.househub.backend.domain.property.enums.PropertyType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyUpdateReqDto {
	private Long customerId;
	private PropertyType propertyType;
	private String roadAddress; // 도로명 주소
	private String jibunAddress; // 지번 주소
	private String detailAddress; // 상세 주소
	private String memo;
	private Boolean active; // 매물 활성화 여부
}
