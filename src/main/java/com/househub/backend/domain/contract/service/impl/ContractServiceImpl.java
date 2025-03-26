package com.househub.backend.domain.contract.service.impl;

import com.househub.backend.domain.contract.dto.CreateContractReqDto;
import com.househub.backend.domain.contract.dto.CreateContractResDto;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.repository.ContractRepository;
import com.househub.backend.domain.contract.service.ContractService;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.repository.PropertyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public CreateContractResDto createContract(CreateContractReqDto dto) {
        // 1. 매물 조회
        Property property = propertyRepository.findById(dto.getPropertyId())
                .orElseThrow(() -> new EntityNotFoundException("해당 매물을 찾을 수 없습니다. ID: " + dto.getPropertyId()));

        // 2. dto → entity 변환 후 저장
        Contract contract = dto.toEntity(property);
        contractRepository.save(contract);

        // 응답 객체 리턴
        return new CreateContractResDto(contract.getContractId());
    }
}
