package com.househub.backend.domain.crawlingProperty.repository;

import com.househub.backend.domain.crawlingProperty.entity.CrawlingProperty;
import com.househub.backend.domain.crawlingProperty.enums.TransactionType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CrawlingPropertyRepository extends JpaRepository<CrawlingProperty, String> {

    // findAll
    @Query("SELECT cp FROM CrawlingProperty cp " +
            "WHERE (:transactionType IS NULL OR cp.transactionType = :transactionType) " +
            "AND (:province IS NULL OR cp.province = :province) " +
            "AND (:city IS NULL OR cp.city = :city) " +
            "AND (:dong IS NULL OR cp.dong LIKE %:#{#dong}%)")
    Page<CrawlingProperty> findByDto(
            @Param("transactionType") TransactionType transactionType,
            @Param("province") String province,
            @Param("city") String city,
            @Param("dong") String dong,
            Pageable pageable
    );

    // findOne
    Optional<CrawlingProperty> findById(String id);
}
