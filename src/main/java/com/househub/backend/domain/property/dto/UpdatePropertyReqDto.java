package com.househub.backend.domain.property.dto;

import com.househub.backend.domain.property.enums.PropertyDirection;
import com.househub.backend.domain.property.enums.PropertyType;

import lombok.Getter;

import java.util.List;

@Getter
public class UpdatePropertyReqDto {
	private Long customerId; // 의뢰인 ID
	private PropertyType propertyType; // 매물 유형
	private String memo;
	private String roadAddress; // 도로명 주소
	private String jibunAddress; // 지번 주소
	private String detailAddress; // 상세 주소
	private Double area; // 면적 (평수)
	private Integer floor; // 층수
	private Integer allFloors; // 총 층수
	private PropertyDirection direction; // 방향 (남, 북, 동, 서, ...)
	private Integer bathroomCnt; // 욕실 개수
	private Integer roomCnt; // 방 개수
	private Boolean active; // 매물이 계약 가능한지 여부
	private List<Long> tagIds;
}
