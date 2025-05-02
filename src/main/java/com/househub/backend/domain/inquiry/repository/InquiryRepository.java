package com.househub.backend.domain.inquiry.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.inquiry.entity.Inquiry;

import org.springframework.data.repository.query.Param;  // Spring Data JPA의 @Param

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
	@Query("""
			SELECT i
			FROM Inquiry i
			LEFT JOIN FETCH i.customer c
			WHERE
				(c.agent.id = :agentId)
				AND (
					:keyword IS NULL OR
					LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
					LOWER(c.contact) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
					LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
				)
		""")
	Page<Inquiry> findInquiriesWithKeyword(
		@Param("agentId") Long agentId,
		@Param("keyword") String keyword,
		Pageable pageable
	);



	@Query("SELECT i FROM Inquiry i " +
		"LEFT JOIN FETCH i.customer c " +
		"LEFT JOIN FETCH i.answers a " +
		"LEFT JOIN FETCH a.question q " +
		"WHERE i.id = :inquiryId")
	Optional<Inquiry> findWithDetailsById(@Param("inquiryId") Long inquiryId);

	@Query("""
			SELECT i
			FROM Inquiry i
			LEFT JOIN FETCH i.customer c
			WHERE
				(c.agent.id = :agentId)
				AND (:customerId = c.id)
		""")
	Page<Inquiry> findInquiriesWithCustomer(
		@Param("agentId") Long agentId,
		@Param("customerId") Long customerId,
		Pageable pageable
	);
}
