package com.househub.backend.domain.property.service;

import com.househub.backend.domain.property.dto.CreatePropertyDto;
import com.househub.backend.domain.property.dto.ResponseDto;
import com.househub.backend.domain.property.dto.UpdatePropertyDto;

import java.util.List;

public interface PropertyService {
    // 매물 등록
    public ResponseDto.PostResponse createProperty(CreatePropertyDto postPropertyDto);

    // 매물 상세 조회
    public ResponseDto findProperty(Long id);

    // 매물 전체 조회
    public List<ResponseDto> findProperties();

    // 매물 정보 수정
    public ResponseDto updateProperty(UpdatePropertyDto updatePropertyDto);

    // 매물 삭제
    public void deleteProperty(Long id);
}
