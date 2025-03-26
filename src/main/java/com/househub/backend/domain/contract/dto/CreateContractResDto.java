package com.househub.backend.domain.contract.dto;

import lombok.Getter;

@Getter
public class CreateContractResDto {
    private Long contractId;
    public CreateContractResDto(Long contractId) {
        this.contractId = contractId;
    }
}
