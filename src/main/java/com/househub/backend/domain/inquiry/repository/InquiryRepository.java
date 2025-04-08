package com.househub.backend.domain.inquiry.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.inquiry.entity.Inquiry;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
	@Query("""
			SELECT i
			FROM Inquiry i
			LEFT JOIN FETCH i.customer c
			LEFT JOIN FETCH i.candidate cc
			WHERE
				(c.agent.id = :agentId OR (c IS NULL AND cc IS NOT NULL))
				AND (
					:keyword IS NULL OR
					LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
					LOWER(c.contact) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
					LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
					LOWER(cc.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
					LOWER(cc.contact) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
					LOWER(cc.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
				)
		""")
	Page<Inquiry> findInquiriesWithCustomerOrCandidate(
		@Param("agentId") Long agentId,
		@Param("keyword") String keyword,
		Pageable pageable
	);

}
