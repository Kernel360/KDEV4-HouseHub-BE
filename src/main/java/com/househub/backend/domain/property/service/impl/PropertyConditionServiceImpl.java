package com.househub.backend.domain.property.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.domain.property.dto.propertyCondition.PropertyConditionListResDto;
import com.househub.backend.domain.property.dto.propertyCondition.PropertyConditionReqDto;
import com.househub.backend.domain.property.dto.propertyCondition.PropertyConditionResDto;
import com.househub.backend.domain.property.dto.propertyCondition.PropertyConditionUpdateReqDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.entity.PropertyCondition;
import com.househub.backend.domain.property.service.PropertyConditionService;
import com.househub.backend.domain.property.service.PropertyReader;
import com.househub.backend.domain.property.service.PropertyStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropertyConditionServiceImpl implements PropertyConditionService {

	private final PropertyReader propertyReader;
	private final PropertyStore propertyStore;

	/**
	 * 매물 조건 추가
	 * @param reqDto 매물 조건 등록 정보 DTO
	 */
	@Transactional
	@Override
	public void createPropertyCondition(Long propertyId, PropertyConditionReqDto reqDto) {
		Property property = propertyReader.findPropertyBy(propertyId);
		PropertyCondition propertyCondition = reqDto.toEntity(property);
		propertyStore.createCondition(propertyCondition);
	}

	/**
	 * 매물 조건 수정
	 * @param propertyId 매물 ID
	 * @param propertyConditionId 매물 조건 ID
	 * @param propertyConditionReqDto 매물 조건 수정 정보 DTO
	 */
	@Transactional
	@Override
	public void updatePropertyCondition(Long propertyId, Long propertyConditionId,
		PropertyConditionUpdateReqDto propertyConditionReqDto) {
		propertyReader.findPropertyBy(propertyId);
		PropertyCondition propertyCondition = propertyReader.findPropertyConditionBy(propertyConditionId);
		propertyCondition.update(propertyConditionReqDto);
	}

	/**
	 * 해당 매물에 대한 매물 조건 조회
	 * @param propertyId 매물 ID
	 * @return 매물 조건 리스트 DTO
	 */
	@Transactional(readOnly = true)
	@Override
	public PropertyConditionListResDto findPropertyConditions(Long propertyId, Pageable pageable) {
		Property property = propertyReader.findPropertyBy(propertyId);
		Page<PropertyCondition> propertyConditions = propertyReader.findPropertyConditionsByPropertyId(property.getId(), pageable);
		Page<PropertyConditionResDto> propertyConditionResDtos = propertyConditions.map(PropertyConditionResDto::fromEntity);
		return PropertyConditionListResDto.fromPage(propertyConditionResDtos);
	}

	/**
	 * 매물 조건 삭제
	 * @param propertyConditionId 매물 조건 ID
	 */
	@Transactional
	@Override
	public void deletePropertyCondition(Long propertyId, Long propertyConditionId) {
		PropertyCondition propertyCondition = propertyReader.findPropertyConditionBy(propertyConditionId);
		propertyCondition.softDelete();
	}
}
