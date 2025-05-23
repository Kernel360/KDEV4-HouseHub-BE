package com.househub.backend.domain.customer.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.agent.entity.Agent;
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

	@Query("SELECT c FROM Customer c WHERE c.agent.id = :agentId AND c.createdAt >= :sevenDaysAgo")
	Page<Customer> findNewCustomersInLast7DaysByAgentId(
			@Param("agentId") Long agentId,
			@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo,
			Pageable pageable
	);


	Optional<Customer> findByEmailAndAgentIdAndDeletedAtIsNull(String email, Long agentId);

	@Query("SELECT c FROM Customer c " +
		"WHERE FUNCTION('MONTH', c.birthDate) = FUNCTION('MONTH', CURRENT_DATE) " +
		"AND FUNCTION('DAY', c.birthDate) = FUNCTION('DAY', CURRENT_DATE)")
	List<Customer> findByBirthDate();

	Optional<Customer> findByIdAndAgentId(Long id, Long agentId);

	Long agent(Agent agent);
}
