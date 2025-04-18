package com.househub.backend.domain.contract.service.impl;

import org.springframework.stereotype.Component;

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
}
