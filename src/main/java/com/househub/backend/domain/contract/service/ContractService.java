package com.househub.backend.domain.contract.service;

import com.househub.backend.domain.contract.dto.*;

import java.util.List;

public interface ContractService {
    // 계약 등록
    public CreateContractResDto createContract(ContractReqDto contractReqDto);

    // 계약 수정
    public void updateContract(Long contractId, ContractReqDto dto);

    // 계약 목록 조회
    public List<FindContractResDto> findContracts(int page, int size);

    // 계약 삭제
    public void deleteContract(Long id);
}
