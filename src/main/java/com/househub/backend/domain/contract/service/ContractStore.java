package com.househub.backend.domain.contract.service;

import com.househub.backend.domain.contract.dto.UpdateContractReqDto;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.customer.entity.Customer;

public interface ContractStore {
	Contract create(Contract contract); // 계약 생성

	void update(Contract contract, UpdateContractReqDto dto); // 계약 수정

	void updateCustomer(Contract contract, Customer customer); // 계약 수정

	void delete(Contract contract); // 계약 삭제
}
