package com.househub.backend.domain.contract.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.contract.dto.ContractSearchDto;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.repository.ContractRepository;
import com.househub.backend.domain.contract.service.ContractReader;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.entity.Property;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ContractReaderImpl implements ContractReader {

	private final ContractRepository contractRepository;

	@Override
	public Page<Contract> searchContracts(Long agentId, ContractSearchDto searchDto, Pageable pageable) {
		Page<Contract> contractPage = contractRepository.findContractsByAgentAndFilters(
			agentId,
			searchDto.getAgentName(),
			searchDto.getCustomerName(),
			searchDto.getContractType(),
			searchDto.getStatus(),
			pageable
		);
		return contractPage;
	}

	/**
	 * 계약 등록 시, 동일 매물에 대해 계약중인 계약이 있는 경우 예외 처리
	 * @param customer 계약을 하는 고객
	 * @param property 계약하는 매물
	 * @param status 등록하려는 계약 상태
	 */
	@Override
	public void existsInProgressContract(Property property, Customer customer, ContractStatus status) {
		// 등록하려는 계약 상태가 완료나 취소인 경우, 계약 가능
		// 등록하려는 계약 상태가 진행중인 경우, 다른 진행중인 계약이 있는지 확인
		if (status == ContractStatus.IN_PROGRESS) {
			boolean isExist = contractRepository.existsByPropertyCondition_PropertyAndCustomerAndStatus(property, customer,
				ContractStatus.IN_PROGRESS);
			if (isExist) {
				throw new BusinessException(ErrorCode.EXISTING_ACTIVE_CONTRACT_FOR_PROPERTY);
			}
		}
	}

	@Override
	public Contract findBy(Long contractId) {
		Contract contract = contractRepository.findById(contractId)
			.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 계약입니다.", "CONTRACT_NOT_FOUND"));
		return contract;
	}
}
