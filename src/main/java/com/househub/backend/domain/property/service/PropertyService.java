package com.househub.backend.domain.property.service;

import com.househub.backend.domain.property.dto.*;

import java.util.List;

public interface PropertyService {
    // 매물 등록
    public CreatePropertyResDto createProperty(PropertyReqDto createPropertyDto);

    // 매물 상세 조회
    public FindPropertyDetailResDto findProperty(Long id);

    // 매물 전체 조회
    public List<FindPropertyResDto> findProperties(int page, int size);

    // 매물 정보 수정
    public void updateProperty(Long propertyId, PropertyReqDto updatePropertyReqDto);

    // 매물 삭제
    public void deleteProperty(Long id);
}
