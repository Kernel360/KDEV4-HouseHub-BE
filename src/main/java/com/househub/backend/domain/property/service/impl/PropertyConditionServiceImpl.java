package com.househub.backend.domain.property.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.property.dto.propertyCondition.PropertyConditionListResDto;
import com.househub.backend.domain.property.dto.propertyCondition.PropertyConditionReqDto;
import com.househub.backend.domain.property.dto.propertyCondition.PropertyConditionResDto;
import com.househub.backend.domain.property.dto.propertyCondition.PropertyConditionUpdateReqDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.entity.PropertyCondition;
import com.househub.backend.domain.property.repository.PropertyConditionRepository;
import com.househub.backend.domain.property.repository.PropertyRepository;
import com.househub.backend.domain.property.service.PropertyConditionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropertyConditionServiceImpl implements PropertyConditionService {

	private final PropertyConditionRepository propertyConditionRepository;
	private final PropertyRepository propertyRepository;

	/**
	 * 매물 조건 추가
	 * @param reqDto 매물 조건 등록 정보 DTO
	 */
	@Transactional
	@Override
	public void createPropertyCondition(Long propertyId, PropertyConditionReqDto reqDto) {
		Property property = findPropertyById(propertyId);
		PropertyCondition propertyCondition = reqDto.toEntity(property);
		propertyConditionRepository.save(propertyCondition);
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
		Property property = findPropertyById(propertyId);
		PropertyCondition propertyCondition = findPropertyConditionById(propertyConditionId);
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
		Property property = findPropertyById(propertyId);
		Page<PropertyCondition> propertyConditions = propertyConditionRepository.findAllByPropertyId(property.getId(), pageable);
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
		PropertyCondition propertyCondition = findPropertyConditionById(propertyConditionId);
		propertyConditionRepository.delete(propertyCondition);
	}

	public PropertyCondition findPropertyConditionById(Long propertyConditionId) {
		return propertyConditionRepository.findById(propertyConditionId)
				.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 매물 조건입니다.", "PROPERTY_NOT_FOUND"));
	}

	/**
	 * 해당 매물 id로 존재 여부 확인
	 * @param id 매물 ID
	 * @return 매물 ID로 매물을 찾았을 경우, Property 리턴
	 *         매물을 찾지 못했을 경우, exception 처리
	 */
	public Property findPropertyById(Long id) {
		Property property = propertyRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 매물입니다.", "PROPERTY_NOT_FOUND"));
		return property;
	}
}
