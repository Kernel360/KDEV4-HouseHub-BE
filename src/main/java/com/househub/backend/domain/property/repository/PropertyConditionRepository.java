package com.househub.backend.domain.property.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.property.entity.PropertyCondition;

@Repository
public interface PropertyConditionRepository extends JpaRepository<PropertyCondition, Long> {
	Page<PropertyCondition> findAllByPropertyId(Long propertyId, Pageable pageable);
}
