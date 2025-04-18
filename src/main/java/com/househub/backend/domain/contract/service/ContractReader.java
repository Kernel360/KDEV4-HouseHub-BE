package com.househub.backend.domain.contract.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.contract.dto.ContractSearchDto;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.entity.Property;

public interface ContractReader {
	Page<Contract> searchContracts(Long agentId, ContractSearchDto searchDto, Pageable pageable);

	void existsInProgressContract(Property property, Customer customer, ContractStatus status);

	Contract findBy(Long contractId);
}
