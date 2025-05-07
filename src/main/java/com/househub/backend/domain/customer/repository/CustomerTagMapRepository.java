package com.househub.backend.domain.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.househub.backend.domain.customer.entity.CustomerTagMap;

public interface CustomerTagMapRepository extends JpaRepository<CustomerTagMap,Long> {

	@Modifying
	@Query("DELETE FROM CustomerTagMap c WHERE c.customer.id = :customerId")
	void deleteByCustomerId(@Param("customerId") Long customerId);
}
