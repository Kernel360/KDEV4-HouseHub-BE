package com.househub.backend.domain.property.service;

import com.househub.backend.domain.property.dto.PostPropertyDto;
import com.househub.backend.domain.property.dto.ResponseDto;

public interface PropertyService {
    // 매물 등록
    public ResponseDto.PostResponse createProperty(PostPropertyDto postPropertyDto);
}
