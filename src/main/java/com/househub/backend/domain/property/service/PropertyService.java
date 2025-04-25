package com.househub.backend.domain.property.service;

import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.property.dto.*;

import org.springframework.data.domain.Pageable;

public interface PropertyService {
    // 매물 등록
    public CreatePropertyResDto createProperty(CreatePropertyReqDto createPropertyDto, AgentResDto agentDto);

    // 매물 상세 조회
    public FindPropertyDetailResDto findProperty(Long propertyId, AgentResDto agentDto);

    // 매물 전체 조회
    public PropertyListResDto findProperties(PropertySearchDto searchDto, Pageable pageable, AgentResDto agentDto);

    // 매물 정보 수정
    public void updateProperty(Long propertyId, UpdatePropertyReqDto updatePropertyReqDto, AgentResDto agentDto);

    // 매물 삭제
    public void deleteProperty(Long propertyId, AgentResDto agentDto);
}
