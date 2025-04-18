package com.househub.backend.domain.contract.service;

import com.househub.backend.domain.contract.entity.Contract;

public interface ContractStore {
	Contract create(Contract contract);
}
