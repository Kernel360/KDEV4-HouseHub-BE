package com.househub.backend.domain.property.dto;

import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreatePropertyReqDto {
    @NotNull
    private PropertyType propertyType;
    private String memo;
    private String roadAddress; // 도로명 주소
    private String jibunAddress; // 지번 주소
    private String detailAddress; // 상세 주소

    private BigDecimal latitude;
    private BigDecimal longitude;

    public Property toEntity() {
        Property property = Property.builder()
                .propertyType(this.propertyType)
                .memo(this.memo)
                .roadAddress(this.roadAddress)
                .detailAddress(this.detailAddress)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .build();
        property.parseJibunAddress(this.jibunAddress);
        return property;
    }

}
