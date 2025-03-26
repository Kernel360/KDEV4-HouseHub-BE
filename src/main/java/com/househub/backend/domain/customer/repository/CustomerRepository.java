package com.househub.backend.domain.customer.repository;

import com.househub.backend.domain.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    List<Customer> findAllByDeletedAtIsNull();

    // deletedAt이 null인 특정 ID의 고객 조회
    Optional<Customer> findByIdAndDeletedAtIsNull(Long id);
}
