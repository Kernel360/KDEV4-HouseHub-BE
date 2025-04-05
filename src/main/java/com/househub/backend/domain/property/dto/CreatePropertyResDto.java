package com.househub.backend.domain.property.dto;

import lombok.Getter;

@Getter
public class CreatePropertyResDto {

    private Long id;

    public CreatePropertyResDto(Long id) {
        this.id = id;
    }
}
