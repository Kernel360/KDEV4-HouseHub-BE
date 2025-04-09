package com.househub.backend.domain.customer.repository;

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
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByEmail(String email);

	Optional<Customer> findByIdAndAgentAndDeletedAtIsNull(Long id, Agent agent);

	Optional<Customer> findByEmailAndContact(String email, String phone);

	@Query(value = "SELECT c FROM Customer c " +
		"WHERE (c.agent = :agentId AND c.deletedAt IS NULL)" +
		"AND (:keyword IS NULL OR c.name LIKE %:keyword% " +
		"OR :keyword IS NULL OR c.contact LIKE %:keyword% " +
		"OR :keyword IS NULL OR c.email LIKE %:keyword%) " +
		"ORDER BY c.createdAt DESC"
	)
	Page<Customer> findAllByAgentAndFiltersAndDeletedAtIsNull(
		@Param("agentId") Long agentId,
		@Param("keyword") String keyword,
		Pageable pageable);
}
