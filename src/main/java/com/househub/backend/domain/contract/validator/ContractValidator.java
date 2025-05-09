package com.househub.backend.domain.contract.validator;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.repository.ContractRepository;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.entity.Property;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractValidator {
    private final ContractRepository contractRepository;

    // 진행중인 계약이 있는지 확인
    /**
     * 해당 매물의 계약 리스트 중 해당 고객이 계약중인 계약이 있으면 예외 처리
     * @param customer 계약을 하는 고객
     * @param property 계약하는 매물
     */
    public void validateNoInProgressContract(Customer customer, Property property) {
        boolean isExist = contractRepository.existsByCustomerAndPropertyAndStatus(customer, property,
                ContractStatus.IN_PROGRESS);
        if (isExist) {
            throw new AlreadyExistsException("해당 고객은 본 매물에 대해 진행중인 계약이 존재합니다.",
                    "CONTRACT_ALREADY_EXISTS");
        }
    }
}
