package com.househub.backend.domain.contract.dto;

import lombok.Getter;

@Getter
public class CreateContractResDto {
    private Long id;
    public CreateContractResDto(Long id) {
        this.id = id;
    }

    public static CreateContractResDto toDto(Long id) {
        return new CreateContractResDto(id);
    }
}
