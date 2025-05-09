package com.househub.backend.domain.property.service;

import java.util.List;

import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.property.dto.*;
import com.househub.backend.domain.property.entity.Property;

import org.springframework.data.domain.Pageable;

public interface PropertyService {
	// 매물 등록
	public CreatePropertyResDto createProperty(CreatePropertyReqDto createPropertyDto, AgentResDto agentDto);

	// 매물 상세 조회
	public FindPropertyDetailResDto findProperty(Long propertyId, AgentResDto agentDto);

	// 매물 전체 조회
	public PropertyListResDto findProperties(PropertySearchDto searchDto, Pageable pageable, AgentResDto agentDto);

	// 특정 고객의 매물 전체 조회
	List<Property> findPropertiesByCustomer(Long customerId, Pageable pageable, Long agentId);

	// 매물 정보 수정
	public void updateProperty(Long propertyId, UpdatePropertyReqDto updatePropertyReqDto, AgentResDto agentDto);

	// 매물 삭제
	public void deleteProperty(Long propertyId, AgentResDto agentDto);

	// 추천 매물 조회
    List<FindPropertyResDto> findRecommendProperties(Long customerId, int limit, AgentResDto agentDto);
}
