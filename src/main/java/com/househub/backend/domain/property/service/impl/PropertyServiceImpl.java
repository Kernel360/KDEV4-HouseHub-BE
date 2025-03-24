package com.househub.backend.domain.property.service.impl;

import com.househub.backend.domain.property.dto.PostPropertyDto;
import com.househub.backend.domain.property.dto.ResponseDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.repository.PropertyRepository;
import com.househub.backend.domain.property.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    @Override
    public ResponseDto.PostResponse createProperty(PostPropertyDto dto) {
        // dto -> entity
        Property property = dto.toEntity();

        // db에 저장
        propertyRepository.save(property);

        // 응답 객체 생성
        ResponseDto.PostResponse result = new ResponseDto.PostResponse(property.getPropertyId());
        return result;
    }


}
