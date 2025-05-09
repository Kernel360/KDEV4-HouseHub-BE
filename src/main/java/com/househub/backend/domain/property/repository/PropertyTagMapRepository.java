package com.househub.backend.domain.property.repository;

import com.househub.backend.domain.property.entity.PropertyTagMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PropertyTagMapRepository extends JpaRepository<PropertyTagMap, Long> {
    @Modifying
    @Query("DELETE FROM PropertyTagMap p WHERE p.property.id = :propertyId")
    void deleteByPropertyId(@Param("propertyId") Long propertyId);
}
