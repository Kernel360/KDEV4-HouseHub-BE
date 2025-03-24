package com.househub.backend.domain.property.service.impl;

import com.househub.backend.domain.property.dto.CreatePropertyReqDto;
import com.househub.backend.domain.property.dto.CreatePropertyResDto;
//import com.househub.backend.domain.property.dto.FindPropertyResDto;
//import com.househub.backend.domain.property.dto.UpdatePropertyReqDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.repository.PropertyRepository;
import com.househub.backend.domain.property.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    @Override // 매물 등록
    public CreatePropertyResDto createProperty(CreatePropertyReqDto dto) {
        // dto -> entity
        Property property = dto.toEntity();

        // db에 저장
        propertyRepository.save(property);

        // 응답 객체 리턴
        return new CreatePropertyResDto(property.getPropertyId());
    }

//    @Override // 매물 상세 조회
//    public FindPropertyResDto findProperty(Long id) {
//        // 해당 entity 찾아오기
//        Property property = propertyRepository.findById(id).get();
//        if(property.getDeletedAt() != null) { // 소프트 삭제일 경우
//            return null;
//        } else {
//            return FindPropertyResDto.toDto(property);
//        }
//    }
//
//    @Override // 매물 전체 조회
//    public List<FindPropertyResDto> findProperties() {
//        return List.of();
//    }
//
//    @Override // 매물 정보 수정
//    public void updateProperty(UpdatePropertyReqDto updatePropertyReqDto) {
//
//    }

    @Override // 매물 삭제
    public void deleteProperty(Long id) {

    }


}
