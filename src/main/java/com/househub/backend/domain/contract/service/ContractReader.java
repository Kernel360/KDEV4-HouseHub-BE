package com.househub.backend.domain.contract.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.contract.dto.ContractSearchDto;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.entity.Property;

public interface ContractReader {
	Contract findByIdOrThrow(Long id, Long agentId); // 계약 ID로 계약 조회

	Page<Contract> findPageBySearchDto(ContractSearchDto searchDto, Pageable pageable, Long agentId); // 계약 검색

	Page<Contract> findContractsByAgentAndCustomer(Long agentId, Long customerId, Pageable pageable);

	Page<Contract> findContractsByProperties(Long agentId, List<Property> propertyIds, Pageable pageable);
}
