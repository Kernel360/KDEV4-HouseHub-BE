package com.househub.backend.domain.contract.service;

import com.househub.backend.domain.contract.dto.*;
import com.househub.backend.domain.property.entity.Property;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContractService {
    // 계약 등록
    public CreateContractResDto createContract(ContractReqDto contractReqDto, Long agentId);

    // 계약 수정
    public void updateContract(Long id, ContractReqDto dto);

    // 계약 목록 조회
    public ContractListResDto findContracts(ContractSearchDto searchDto, Pageable pageable, Long agentId);

    // 고객 계약 내역 조회
    public ContractListResDto findAllByCustomer(Long id, String customerName, Pageable pageable, Long agentId);

    // 매물 계약 내역 조회
    public ContractListResDto findAllByProperties(List<Property> propertyIds, Pageable pageable, Long agentId);

    // 계약 상세 조회
    public FindContractResDto findContract(Long id);

    // 계약 삭제
    public void deleteContract(Long id);
}
