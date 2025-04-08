package com.househub.backend.domain.crawlingProperty.repository;

import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyResDto;
import com.househub.backend.domain.crawlingProperty.entity.CrawlingProperty;
import com.househub.backend.domain.crawlingProperty.enums.PropertyType;
import com.househub.backend.domain.crawlingProperty.enums.TransactionType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CrawlingPropertyRepository extends JpaRepository<CrawlingProperty, String> {

    // findAll
    @Query("SELECT cp FROM CrawlingProperty cp " +
            "WHERE (:propertyType IS NULL OR cp.propertyType = :propertyType) " +
            "AND (:transactionType IS NULL OR cp.transactionType = :transactionType) " +
            "AND (:province IS NULL OR cp.province = :province) " +
            "AND (:city IS NULL OR cp.city = :city) " +
            "AND (:dong IS NULL OR cp.dong LIKE %:#{#dong}%)" +
            "AND (:detailAddress IS NULL OR cp.detailAddress LIKE %:#{#detailAddress}%) " +

            "AND (:minArea IS NULL OR cp.area >= :minArea) " +
            "AND (:maxArea IS NULL OR cp.area <= :maxArea) " +

            "AND (:minSalePrice IS NULL OR cp.salePrice >= :minSalePrice) " +
            "AND (:maxSalePrice IS NULL OR cp.salePrice <= :maxSalePrice) " +

            "AND (:minDeposit IS NULL OR cp.deposit >= :minDeposit) " +
            "AND (:maxDeposit IS NULL OR cp.deposit <= :maxDeposit) " +

            "AND (:minMonthlyRent IS NULL OR cp.monthlyRentFee >= :minMonthlyRent) " +
            "AND (:maxMonthlyRent IS NULL OR cp.monthlyRentFee <= :maxMonthlyRent)")
    Page<CrawlingProperty> findByDto(
            @Param("propertyType") PropertyType propertyType,
            @Param("transactionType") TransactionType transactionType,
            @Param("province") String province,
            @Param("city") String city,
            @Param("dong") String dong,
            @Param("detailAddress") String detailAddress,

            @Param("minArea") Float minArea,
            @Param("maxArea") Float maxArea,

            @Param("minSalePrice") Float minSalePrice,
            @Param("maxSalePrice") Float maxSalePrice,

            @Param("minDeposit") Float minDeposit,
            @Param("maxDeposit") Float maxDeposit,

            @Param("minMonthlyRent") Float minMonthlyRent,
            @Param("maxMonthlyRent") Float maxMonthlyRent,

            Pageable pageable
    );

    // findOne
    Optional<CrawlingProperty> findById(String id);
}
