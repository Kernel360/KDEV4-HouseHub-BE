package com.househub.backend.domain.property.dto;

import com.househub.backend.domain.contract.dto.FindContractResDto;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class FindPropertyDetailResDto {

    private Long id; // 매물 고유 식별자
    private PropertyType propertyType; // 매물 유형
    private CreateCustomerResDto customer; // 의뢰인(임대인 또는 매도인)
    private String memo; // 참고 설명
    private String province; // 도, 특별시, 광역시
    private String city; // 시/군/구
    private String dong; // 읍/면/동
    private String detailAddress; // 상세 주소
    private String roadAddress; // 전체 도로명 주소
    private LocalDateTime createdAt; // 등록일시
    private LocalDateTime updatedAt; // 수정일시
    private BigDecimal latitude; // 위도
    private BigDecimal longitude; // 경도
    private List<FindContractResDto> contractList;

    // Entity -> DTO 변환
    public static FindPropertyDetailResDto toDto(Property property) {
        return FindPropertyDetailResDto.builder()
                .id(property.getId())
                .propertyType(property.getPropertyType())
                .customer(property.getCustomer().toDto())
                .memo(property.getMemo())
                .province(property.getProvince())
                .city(property.getCity())
                .dong(property.getDong())
                .detailAddress(property.getDetailAddress())
                .roadAddress(property.getRoadAddress())
                .createdAt(property.getCreatedAt())
                .updatedAt(property.getUpdatedAt())
                .latitude(property.getLatitude())
                .longitude(property.getLongitude())
                // dto 로 변환 후 반환
                .contractList(property.getContracts() != null ?
                        property.getContracts().stream().map(FindContractResDto::toDto).toList() : null)
                .build();
    }
}
