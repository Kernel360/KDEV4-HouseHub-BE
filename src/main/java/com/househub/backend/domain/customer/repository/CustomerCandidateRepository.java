package com.househub.backend.domain.customer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.househub.backend.domain.customer.entity.CustomerCandidate;

public interface CustomerCandidateRepository extends JpaRepository<CustomerCandidate, Long> {
	Optional<CustomerCandidate> findByEmailAndContact(String email, String phone);
}