package com.househub.backend.domain.property.dto;

import com.househub.backend.domain.property.enums.PropertyType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;
@Getter
public class UpdatePropertyReqDto {
    @NotNull
    private PropertyType propertyType;
    private String memo;
    @NotNull
    private String roadAddress; // 도로명 주소
    @NotNull
    private String jibunAddress; // 지번 주소
    @NotNull
    private String detailAddress; // 상세 주소

    private BigDecimal latitude;
    private BigDecimal longitude;
}
