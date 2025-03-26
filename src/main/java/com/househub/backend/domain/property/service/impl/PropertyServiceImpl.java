package com.househub.backend.domain.property.service.impl;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.domain.property.dto.CreatePropertyReqDto;
import com.househub.backend.domain.property.dto.CreatePropertyResDto;
import com.househub.backend.domain.property.dto.UpdatePropertyReqDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.repository.PropertyRepository;
import com.househub.backend.domain.property.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    /**
     *
     * @param dto 매물 등록 정보 DTO
     * @return 등록된 매물 id 를 반환하는 DTO
     */
    @Transactional
    @Override // 매물 등록
    public CreatePropertyResDto createProperty(CreatePropertyReqDto dto) {
        // dto -> entity
        Property property = dto.toEntity();

        // 전체 주소(도로명 주소 + 상세 주소)로 해당 매물이 이미 존재하는지 확인
        isExistsProperty(property);

        // db에 저장
        propertyRepository.save(property);

        // 응답 객체 리턴
        return new CreatePropertyResDto(property.getPropertyId());
    }


    /**
     *
     * @param propertyId 매물 id
     * @param updateDto 수정된 매물 정보 DTO
     */
    @Transactional
    @Override // 매물 정보 수정
    public void updateProperty(Long propertyId, UpdatePropertyReqDto updateDto) {
        // 매물 조회
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        isExistsProperty(property);
        property.updateProperty(updateDto);
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

    /**
     *
     * @param property
     */
    public void isExistsProperty(Property property) {
        // 전체 주소(도로명 주소 + 상세 주소)로 해당 매물이 이미 존재하는지 확인
        boolean isExist = propertyRepository.existsByRoadAddressAndDetailAddress(property.getRoadAddress(), property.getDetailAddress());
        if(isExist) {
            throw new AlreadyExistsException("이미 존재하는 매물 입니다.", "PROPERTY_ALREADY_EXISTS");
        }
    }
}
