package com.househub.backend.domain.property.service;

import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.property.dto.propertyCondition.PropertyConditionListResDto;
import com.househub.backend.domain.property.dto.propertyCondition.PropertyConditionReqDto;
import com.househub.backend.domain.property.dto.propertyCondition.PropertyConditionUpdateReqDto;

public interface PropertyConditionService {
	// 매물 조건 추가
	public void createPropertyCondition(Long propertyId, PropertyConditionReqDto propertyConditionReqDto);
	// 매물 조건 수정
	public void updatePropertyCondition(Long propertyId, Long propertyConditionId, PropertyConditionUpdateReqDto reqDto);
	// 매물 조건 조회
	public PropertyConditionListResDto findPropertyConditions(Long propertyId, Pageable pageable);
	// 매물 조건 삭제
	public void deletePropertyCondition(Long propertyId, Long propertyConditionId);
}
