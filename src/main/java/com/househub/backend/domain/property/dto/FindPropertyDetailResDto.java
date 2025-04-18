package com.househub.backend.domain.property.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.househub.backend.domain.customer.dto.CreateCustomerResDto;
import com.househub.backend.domain.property.dto.propertyCondition.PropertyConditionResDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyType;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FindPropertyDetailResDto {

    private Long id; // 매물 고유 식별자
    private PropertyType propertyType; // 매물 유형
    private CreateCustomerResDto customer; // 의뢰인(임대인 또는 매도인)
    private String memo; // 참고 설명
    // private String province; // 도, 특별시, 광역시
    // private String city; // 시/군/구
    // private String dong; // 읍/면/동
    private String detailAddress; // 상세 주소
    private String roadAddress; // 전체 도로명 주소
    private String jibunAddress; // 지번 주소
    private LocalDateTime createdAt; // 등록일시
    private LocalDateTime updatedAt; // 수정일시
    // private BigDecimal latitude; // 위도
    // private BigDecimal longitude; // 경도
    private List<PropertyConditionResDto> conditionList; // 매물 조건 리스트 (매물 조건 리스트 안에 계약 리스트 포함)
    private Boolean active; // 매물이 계약 가능한지 여부 default : true (계약이 없는 경우 true)

    // Entity -> DTO 변환
    public static FindPropertyDetailResDto fromEntity(Property property) {
        return FindPropertyDetailResDto.builder()
                .id(property.getId())
                .propertyType(property.getPropertyType())
                .customer(CreateCustomerResDto.fromEntity(property.getCustomer()))
                .memo(property.getMemo())
                // .province(property.getProvince())
                // .city(property.getCity())
                // .dong(property.getDong())
                .detailAddress(property.getDetailAddress())
                .roadAddress(property.getRoadAddress())
                .jibunAddress(property.getJibunAddress())
                .createdAt(property.getCreatedAt())
                .updatedAt(property.getUpdatedAt())
            // 매물 조건 리스트 dto 변환
            .conditionList(property.getConditions() != null ?
                    property.getConditions().stream().map(PropertyConditionResDto::fromEntity).toList() : null)
                .active(property.getActive())
                .build();
    }
}
