package com.househub.backend.domain.contract.service;

import com.househub.backend.domain.contract.dto.CreateContractReqDto;
import com.househub.backend.domain.contract.dto.CreateContractResDto;

public interface ContractService {
    // 계약 등록
    public CreateContractResDto createContract(CreateContractReqDto createContractReqDto);
    // 계약 수정

    // 계약 조회

    // 계약 삭제
}
