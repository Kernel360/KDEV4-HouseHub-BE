package com.househub.backend.domain.property.service.impl;

import com.househub.backend.domain.property.dto.CreatePropertyDto;
import com.househub.backend.domain.property.dto.ResponseDto;
import com.househub.backend.domain.property.dto.UpdatePropertyDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.repository.PropertyRepository;
import com.househub.backend.domain.property.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    @Override // 매물 등록
    public ResponseDto.PostResponse createProperty(CreatePropertyDto dto) {
        // dto -> entity
        Property property = dto.toEntity();

        // db에 저장
        propertyRepository.save(property);

        // 응답 객체 생성
        ResponseDto.PostResponse result = new ResponseDto.PostResponse(property.getPropertyId());
        return result;
    }

    @Override // 매물 상세 조회
    public ResponseDto findProperty(Long id) {
        return null;
    }

    @Override // 매물 전체 조회
    public List<ResponseDto> findProperties() {
        return List.of();
    }

    @Override // 매물 정보 수정
    public ResponseDto updateProperty(UpdatePropertyDto updatePropertyDto) {
        return null;
    }

    @Override // 매물 삭제
    public void deleteProperty(Long id) {

    }


}
