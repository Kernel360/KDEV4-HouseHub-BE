package com.househub.backend.domain.contract.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.househub.backend.common.exception.AlreadyExistsException;
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
	public Contract findByIdOrThrow(Long id, Long agentId) {
		return contractRepository.findByIdAndAgentId(id, agentId)
			.orElseThrow(() -> new ResourceNotFoundException("해당 ID의 계약을 찾을 수 없습니다.", "CONTRACT_NOT_FOUND"));
	}

	@Override
	public Page<Contract> findPageBySearchDto(ContractSearchDto searchDto, Pageable pageable, Long agentId) {
		return contractRepository.findContractsByAgentAndFilters(
			agentId,
			searchDto.getAgentName(),
			searchDto.getCustomerName(),
			searchDto.getContractType(),
			searchDto.getStatus(),
			pageable
		);
	}

	/**
	 * 해당 매물의 계약 리스트 중 해당 고객이 계약중인 계약이 있으면 예외 처리
	 * @param customer 계약을 하는 고객
	 * @param property 계약하는 매물
	 */
	@Override
	public void validateNoInProgressContract(Customer customer, Property property) {
		boolean isExist = contractRepository.existsByCustomerAndPropertyAndStatus(customer, property,
			ContractStatus.IN_PROGRESS);
		if (isExist) {
			throw new AlreadyExistsException("해당 고객은 본 매물에 대해 진행중인 계약이 존재합니다.",
				"CONTRACT_ALREADY_EXISTS");
		}
	}
}
