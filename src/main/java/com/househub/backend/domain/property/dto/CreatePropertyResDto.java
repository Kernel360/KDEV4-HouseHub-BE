package com.househub.backend.domain.property.dto;

import lombok.Getter;

@Getter
public class CreatePropertyResDto {

    private Long propertyId;

    public CreatePropertyResDto(Long propertyId) {
        this.propertyId = propertyId;
    }
}
