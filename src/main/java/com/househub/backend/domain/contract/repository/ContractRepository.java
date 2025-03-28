package com.househub.backend.domain.contract.repository;

import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    // 고객, 매물이 동일한 계약 중 판매중인 계약이 존재하는지 여부 확인
    // true -> 중복 존재, false -> 중복되는 계약 없음
    boolean existsByCustomerAndPropertyAndStatusNot(Customer customer, Property property, ContractStatus status);
}
