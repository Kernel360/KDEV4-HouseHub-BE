package com.househub.backend.domain.property.service.impl;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.property.dto.*;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.repository.PropertyRepository;
import com.househub.backend.domain.property.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public CreatePropertyResDto createProperty(PropertyReqDto dto) {
        // 동일한 주소를 가진 매물 있는지 확인
        existsByAddress(dto.getRoadAddress(), dto.getDetailAddress());
        // dto -> entity
        Property property = dto.toEntity();
        // db에 저장
        propertyRepository.save(property);
        // 응답 객체 리턴
        return new CreatePropertyResDto(property.getPropertyId());
    }

    /**
     *
     * @param id 매물 ID
     * @return 매물 상세 정보 응답 DTO
     */
    @Transactional // 매물 정보 조회 후 계약 리스트 불러오도록
    @Override // 매물 상세 조회
    public FindPropertyDetailResDto findProperty(Long id) {
        // 매물 조회
        Property property = findPropertyById(id);
        // entity -> dto
        // 해당 매물의 계약 리스트도 response 에 포함
        FindPropertyDetailResDto response = FindPropertyDetailResDto.toDto(property);
        return response;
    }

    /**
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 매물 기본 정보 응답 DTO LIST
     */
    @Transactional(readOnly = true)
    @Override // 매물 리스트 조회
    public List<FindPropertyResDto> findProperties(int page, int size) {
        // Pageable 객체 생성 (페이지 번호, 페이지 크기)
        Pageable pageable = PageRequest.of(page, size);

        // 페이지네이션 적용하여 매물 조회
        Page<Property> propertyPage = propertyRepository.findAll(pageable);

        // 매물 엔티티를 dto 로 변환하여 리스트로 반환
        return propertyPage.stream()
                .map(FindPropertyResDto::toDto)
                .toList();
    }


    /**
     *
     * @param propertyId 매물 id
     * @param updateDto 수정된 매물 정보 DTO
     */
    @Transactional
    @Override // 매물 정보 수정
    public void updateProperty(Long propertyId, PropertyReqDto updateDto) {
        // 주소가 동일한 매물이 있는지 확인
        existsByAddress(updateDto.getRoadAddress(), updateDto.getDetailAddress());
        // 매물 조회
        Property property = findPropertyById(propertyId);
        // id로 조회한 매물 정보 수정 및 저장
        property.updateProperty(updateDto);
    }

    /**
     *
     * @param id 매물 ID
     */
    @Transactional
    @Override // 매물 삭제
    public void deleteProperty(Long id) {
        // 매물 조회
        Property property = findPropertyById(id);
        // 매물 소프트 삭제
        property.deleteProperty();
        // 매물 삭제 시, 해당 계약도 모두 소프트 딜리트 해야함
        property.getContracts().forEach(Contract::deleteContract);
    }


    // 전체 주소(도로명 주소 + 상세 주소)로 해당 매물이 이미 존재하는지 확인
    /**
     *
     * @param roadAddress 도로명 주소
     * @param detailAddress 상세 주소
     */
    public void existsByAddress(String roadAddress, String detailAddress) {
        boolean isExist = propertyRepository.existsByRoadAddressAndDetailAddress(roadAddress, detailAddress);
        if(isExist) {
            throw new AlreadyExistsException("이미 존재하는 매물 입니다.", "PROPERTY_ALREADY_EXISTS");
        }
    }

    // 해당 매물 id 존재 여부 확인
    /**
     *
     * @param id 매물 ID
     * @return 매물 ID로 매물을 찾았을 경우, Property 리턴
     *         매물을 찾지 못했을 경우, exception 처리
     */
    public Property findPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 매물입니다.", "PROPERTY_NOT_FOUND"));
        return property;
    }
}
