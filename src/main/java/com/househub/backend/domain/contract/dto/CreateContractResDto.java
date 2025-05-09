package com.househub.backend.domain.contract.dto;

import com.househub.backend.domain.contract.entity.Contract;
import lombok.Getter;

@Getter
public class CreateContractResDto {
    private Long id;
    public CreateContractResDto(Long id) {
        this.id = id;
    }

    public static CreateContractResDto fromEntity(Contract contract) {
        return new CreateContractResDto(contract.getId());
    }
}
