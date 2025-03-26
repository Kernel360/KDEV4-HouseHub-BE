package com.househub.backend.domain.property.service;

import com.househub.backend.domain.property.dto.CreatePropertyReqDto;
import com.househub.backend.domain.property.dto.CreatePropertyResDto;

public interface PropertyService {
    // 매물 등록
    public CreatePropertyResDto createProperty(CreatePropertyReqDto createPropertyDto);

//    // 매물 상세 조회
//    public FindPropertyResDto findProperty(Long id);
//
//    // 매물 전체 조회
//    public List<FindPropertyResDto> findProperties();
//
//    // 매물 정보 수정
//    public void updateProperty(UpdatePropertyReqDto updatePropertyReqDto);

    // 매물 삭제
    public void deleteProperty(Long id);
}
