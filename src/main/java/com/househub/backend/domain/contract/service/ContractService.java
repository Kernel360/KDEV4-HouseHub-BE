package com.househub.backend.domain.contract.service;

import com.househub.backend.domain.contract.dto.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContractService {
    // 계약 등록
    public CreateContractResDto createContract(ContractReqDto contractReqDto, Long agentId);

    // 계약 수정
    public void updateContract(Long id, ContractUpdateReqDto dto);

    // 계약 목록 조회
    public ContractListResDto findContracts(ContractSearchDto searchDto, Pageable pageable, Long agentId);

    // 계약 상세 조회
    public FindContractResDto findContract(Long id);

    // 계약 삭제
    public void deleteContract(Long id);
}
