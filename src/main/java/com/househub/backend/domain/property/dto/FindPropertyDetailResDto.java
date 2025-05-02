package com.househub.backend.domain.property.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.househub.backend.domain.contract.dto.FindContractResDto;
import com.househub.backend.domain.customer.dto.CustomerResDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyDirection;
import com.househub.backend.domain.property.enums.PropertyType;

import com.househub.backend.domain.tag.dto.TagResDto;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FindPropertyDetailResDto {

    private Long id; // 매물 고유 식별자
    private PropertyType propertyType; // 매물 유형
    private CustomerResDto customer; // 의뢰인(임대인 또는 매도인)
    private String memo; // 참고 설명
    private String detailAddress; // 상세 주소
    private String roadAddress; // 전체 도로명 주소
    private String jibunAddress; // 지번 주소
    private List<FindContractResDto> contractList;
    private Boolean active; // 매물이 계약 가능한지 여부 default
    private LocalDateTime createdAt; // 생성 일시
    private Double area; // 면적 (평수)
    private Integer floor; // 층수
    private Integer allFloors; // 총 층수
    private PropertyDirection direction; // 방향 (남, 북, 동, 서 , ...)
    private Integer bathroomCnt; // 욕실 개수
    private Integer roomCnt; // 방 개수
    private List<TagResDto> tags;

    // Entity -> DTO 변환
    public static FindPropertyDetailResDto toDto(Property property) {
        return FindPropertyDetailResDto.builder()
                .id(property.getId())
                .propertyType(property.getPropertyType())
                .customer(CustomerResDto.fromEntity(property.getCustomer()))
                .memo(property.getMemo())
                .detailAddress(property.getDetailAddress())
                .roadAddress(property.getRoadAddress())
                .jibunAddress(property.getJibunAddress())
                .area(property.getArea())
                .floor(property.getFloor())
                .allFloors(property.getAllFloors())
                .direction(property.getDirection())
                .bathroomCnt(property.getBathroomCnt())
                .roomCnt(property.getRoomCnt())
                // dto 로 변환 후 반환
                .contractList(property.getContracts() != null ?
                        property.getContracts().stream().map(FindContractResDto::toDto).toList() : null)
                .active(property.getActive())
                .createdAt(property.getCreatedAt())
                .tags(property.getPropertyTagMaps() == null ? Collections.emptyList() :
                        property.getPropertyTagMaps().stream().map(m -> TagResDto.fromEntity(m.getTag())).toList())
                .build();
    }
}
