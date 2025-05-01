package com.househub.backend.domain.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.househub.backend.domain.customer.entity.CustomerTagMap;

public interface CustomerTagMapRepository extends JpaRepository<CustomerTagMap,Long> {


}
