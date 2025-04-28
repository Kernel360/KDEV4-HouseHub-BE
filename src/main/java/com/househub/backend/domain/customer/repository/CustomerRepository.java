package com.househub.backend.domain.customer.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.consultation.entity.Consultation;
import com.househub.backend.domain.customer.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {

	Optional<Customer> findByContactAndAgentIdAndDeletedAtIsNull(String contact, Long agentId);

	Optional<Customer> findByContactAndAgentId(String contact, Long agentId);

	Optional<Customer> findByIdAndAgentIdAndDeletedAtIsNull(Long id, Long agentId);

	Optional<Customer> findByEmailAndContact(String email, String phone);

	@Query("SELECT COUNT(c) FROM Customer c WHERE c.agent.id = :agentId AND c.createdAt >= :sevenDaysAgo")
	long countNewCustomersInLast7DaysByAgentId(@Param("agentId") Long agentId,
		@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);

	Optional<Customer> findByEmailAndAgentIdAndDeletedAtIsNull(String email, Long agentId);

	List<Customer> findByBirthDate(LocalDate birthDate);

	@Query("SELECT DISTINCT c FROM Customer c " +
		"JOIN c.contracts contract " +
		"WHERE contract.expiredAt = :today " +
		"AND contract.deletedAt IS NULL")
	List<Customer> findCustomersWithExpiringContracts(@Param("today") LocalDate today);

	Customer findByIdAndAgentId(Long id, Long agentId);
}
