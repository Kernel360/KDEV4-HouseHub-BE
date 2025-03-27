package com.househub.backend.domain.contract.service.impl;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.contract.dto.*;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.repository.ContractRepository;
import com.househub.backend.domain.contract.service.ContractService;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.repository.PropertyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepository;
    private final PropertyRepository propertyRepository;

    /**
     *
     * @param dto 해당 매물에 계약 등록하는 DTO
     * @return 등록한 계약 id 를 반환하는 DTO
     */
    @Override
    @Transactional
    public CreateContractResDto createContract(ContractReqDto dto) {
        // 1. 계약할 매물 조회
        Property property = propertyRepository.findById(dto.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 매물입니다.", "PROPERTY_NOT_FOUND"));

        // 2. dto → entity 변환 후 저장
        Contract contract = dto.toEntity(property);
        contractRepository.save(contract);

        // 응답 객체 리턴
        return new CreateContractResDto(contract.getContractId());
    }

    @Override
    public void updateContract(Long contractId, ContractReqDto dto) {

    }

    @Override
    public List<FindContractResDto> findContracts(int page, int size) {
        return List.of();
    }

    @Override
    public FindContractResDto findContract(Long id) {
        return null;
    }

    @Override
    public void deleteContract(Long id) {

    }
}
