package com.househub.backend.domain.realEstate.repository;

import com.househub.backend.domain.agent.entity.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RealEstateRepository extends JpaRepository<RealEstate, Long> {
    Optional<RealEstate> findByBusinessRegistrationNumber(String businessRegistrationNumber);
}
