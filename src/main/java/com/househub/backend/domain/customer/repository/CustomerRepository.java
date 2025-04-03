package com.househub.backend.domain.customer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    List<Customer> findAllByAgentAndDeletedAtIsNull(Agent agent);

    Optional<Customer> findByIdAndAgentAndDeletedAtIsNull(Long id, Agent agent);
}
