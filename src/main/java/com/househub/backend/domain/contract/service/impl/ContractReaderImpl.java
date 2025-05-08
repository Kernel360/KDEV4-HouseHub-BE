package com.househub.backend.domain.contract.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.contract.dto.ContractSearchDto;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.repository.ContractRepository;
import com.househub.backend.domain.contract.service.ContractReader;
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

	@Override
	public Page<Contract> findContractsByAgentAndCustomer(Long agentId, Long customerId, Pageable pageable) {
		return contractRepository.findContractsByAgentAndCustomer(
			agentId,
			customerId,
			pageable
		);
	}

	@Override
	public Page<Contract> findContractsByProperties(Long agentId, List<Property> propertyIds, Pageable pageable) {
		return contractRepository.findContractsByProperties(
			agentId,
			propertyIds,
			pageable
		);
	}

	@Override
	public List<Contract> findAllByExpiredAtBetween(LocalDate startDate,
		LocalDate endDate) {
		return contractRepository.findAllByExpiredAtBetween(startDate, endDate);
	}
}
