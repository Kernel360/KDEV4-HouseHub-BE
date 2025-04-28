package com.househub.backend.domain.contract.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.contract.dto.UpdateContractReqDto;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.repository.ContractRepository;
import com.househub.backend.domain.contract.service.ContractStore;
import com.househub.backend.domain.customer.entity.Customer;

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
	public void update(Contract contract, UpdateContractReqDto dto) {
		contract.update(dto);
	}

	@Override
	public void updateCustomer(Contract contract, Customer customer) {
		contract.updateCustomer(customer);
	}

	@Override
	public void delete(Contract contract) {
		contract.softDelete();
	}
}
