package com.househub.backend.domain.contract.repository;

import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.enums.ContractType;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.entity.Property;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    // 고객, 매물이 동일한 계약 중 판매중인 계약이 존재하는지 여부 확인
    // true -> 중복 존재, false -> 중복되는 계약 없음
    boolean existsByCustomerAndPropertyAndStatusNot(Customer customer, Property property, ContractStatus status);

    // 최근 계약부터 정렬
    @Query("SELECT c FROM Contract c " +
        "JOIN c.agent a " +
        "WHERE a.realEstate.id = :realEstateId " +
        "AND (:agentName IS NULL OR a.name LIKE %:agentName%) " +
        "AND (:customerName IS NULL OR c.customer.name LIKE %:customerName%) " +
        "AND (:contractType IS NULL OR c.contractType = :contractType) " +
        "AND (:status IS NULL OR c.status = :status) " +
        "ORDER BY c.createdAt DESC")
    Page<Contract> findContractsByRealEstateAndFilters(
        @Param("realEstateId") Long realEstateId,
        @Param("agentName") String agentName,
        @Param("customerName") String customerName,
        @Param("contractType") ContractType contractType,
        @Param("status") ContractStatus status,
        Pageable pageable
    );
}
