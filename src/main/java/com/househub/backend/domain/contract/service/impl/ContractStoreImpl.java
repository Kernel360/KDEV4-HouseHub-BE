package com.househub.backend.domain.contract.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.contract.dto.UpdateContractReqDto;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.repository.ContractRepository;
import com.househub.backend.domain.contract.service.ContractStore;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ContractStoreImpl implements ContractStore {

	private final ContractRepository contractRepository;

	@Override
	public Contract create(Contract contract) {
		return contractRepository.save(contract);
	}

	@Override
	public Contract update(Contract contract, UpdateContractReqDto dto) {
		contract.updateContract(dto);
		return contract;
	}

	@Override
	public void delete(Contract contract) {
		contract.softDelete();
	}
}
