package com.househub.backend.domain.customer.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.customer.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByContactAndAgentIdAndDeletedAtIsNull(String contact, Long agentId);

	Optional<Customer> findByIdAndAgentIdAndDeletedAtIsNull(Long id, Long agentId);

	Optional<Customer> findByEmailAndContact(String email, String phone);

	@Query(value = "SELECT c FROM Customer c " +
		"WHERE c.deletedAt IS NULL " +
		"AND c.agent.id = :agentId " +
		"AND (" +
		"   (:name IS NULL OR :name = '' OR c.name LIKE CONCAT('%', :name, '%')) OR " +
		"   (:contact IS NULL OR :contact = '' OR c.contact LIKE CONCAT('%', :contact, '%')) OR " +
		"   (:email IS NULL OR :email = '' OR c.email LIKE CONCAT('%', :email, '%'))" +
		") " +
		"ORDER BY c.createdAt DESC"
	)
	Page<Customer> findAllByAgentIdAndFiltersAndDeletedAtIsNull(
		@Param("agentId") Long agentId,
		@Param("name") String name,
		@Param("contact") String contact,
		@Param("email") String email,
		Pageable pageable);

	@Query("SELECT COUNT(c) FROM Customer c WHERE c.agent.id = :agentId AND c.createdAt >= :sevenDaysAgo")
	long countNewCustomersInLast7DaysByAgentId(@Param("agentId") Long agentId,
		@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);
}
