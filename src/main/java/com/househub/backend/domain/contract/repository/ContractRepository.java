package com.househub.backend.domain.contract.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.enums.ContractType;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.entity.Property;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
	Optional<Contract> findByIdAndAgentId(Long contractId, Long agentId);

	// 같은 계약자가 동일한 매물에 대해 진행중인 계약이 있는지 확인
	// ture -> 있음, false -> 없음
	boolean existsByCustomerAndPropertyAndStatus(Customer customer, Property property, ContractStatus status);

	@Query("SELECT c FROM Contract c " +
		"JOIN c.agent a " +
		"LEFT JOIN c.customer cu " +
		"WHERE a.id = :agentId " +
		"AND (:agentName IS NULL OR a.name LIKE CONCAT('%', :agentName, '%')) " +
		"AND (:customerName IS NULL OR cu.name LIKE CONCAT('%', :customerName, '%')) " +
		"AND (:contractType IS NULL OR c.contractType = :contractType) " +
		"AND (:status IS NULL OR c.status = :status) " +
		"ORDER BY c.createdAt DESC")
	Page<Contract> findContractsByAgentAndFilters(
		@Param("agentId") Long agentId,
		@Param("agentName") String agentName,
		@Param("customerName") String customerName,
		@Param("contractType") ContractType contractType,
		@Param("status") ContractStatus status,
		Pageable pageable
	);

	@Query("SELECT c FROM Contract c WHERE c.agent.id = :agentId")
	Page<Contract> findAllByAgentId(
		@Param("agentId") Long agentId,
		Pageable pageable
	);

	@Query("SELECT COUNT(c) FROM Contract c WHERE c.agent.id = :agentId AND c.status = :status")
	long countByAgentIdAndStatus(@Param("agentId") Long agentId, @Param("status") ContractStatus status);

	long countByAgentIdAndStatusAndCreatedAtBetween(
		Long agentId,
		ContractStatus status,
		LocalDateTime startDate,
		LocalDateTime endDate
	);

	List<Contract> findAllByAgentIdAndCreatedAtBetween(
		Long agentId,
		LocalDateTime startDate,
		LocalDateTime endDate
	);

	@Query("SELECT c FROM Contract c " +
		"JOIN c.agent a " +
		"WHERE a.id = :agentId " +
		"AND :customerId = c.customer.id " +
		"ORDER BY c.createdAt DESC")
	Page<Contract> findContractsByAgentAndCustomer(
		@Param("agentId") Long agentId,
		@Param("customerId") Long customerId,
		Pageable pageable
	);

	@Query("SELECT c FROM Contract c " +
		"JOIN c.agent a " +
		"WHERE a.id = :agentId " +
		"AND (c.property IN :properties)" +
		"ORDER BY c.createdAt DESC")
	Page<Contract> findContractsByProperties(
		@Param("agentId") Long agentId,
		@Param("properties") List<Property> propertyIds,
		Pageable pageable
	);

	@EntityGraph(attributePaths = {"property", "customer"})
	Page<Contract> findByAgentIdAndExpiredAtBetween(
		Long agentId,
		LocalDate startDate,
		LocalDate endDate,
		Pageable pageable
	);
}
