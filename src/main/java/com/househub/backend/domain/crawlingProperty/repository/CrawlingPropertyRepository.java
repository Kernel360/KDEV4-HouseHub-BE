package com.househub.backend.domain.crawlingProperty.repository;

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

public interface CrawlingPropertyRepository extends JpaRepository<CrawlingProperty, String>, CrawlingPropertyRepositoryCustom {

    // findAll
    @Query(
            "SELECT cp " +
                    "FROM CrawlingProperty cp " +
                    "WHERE (:transactionType IS NULL OR cp.transactionType = :transactionType) " +
                    "AND (:propertyType IS NULL OR cp.propertyType = :propertyType) " +
                    "AND (:province IS NULL OR cp.province = :province) " +
                    "AND (:city IS NULL OR cp.city = :city) " +
                    "AND (:dong IS NULL OR cp.dong LIKE CONCAT('%', :dong, '%')) " +
                    "AND (:minSalePrice IS NULL OR cp.salePrice >= :minSalePrice) " +
                    "AND (:maxSalePrice IS NULL OR cp.salePrice <= :maxSalePrice) " +
                    "AND (:minDeposit IS NULL OR cp.deposit >= :minDeposit) " +
                    "AND (:maxDeposit IS NULL OR cp.deposit <= :maxDeposit) " +
                    "AND (:minMonthlyRentFee IS NULL OR cp.monthlyRentFee >= :minMonthlyRentFee) " +
                    "AND (:maxMonthlyRentFee IS NULL OR cp.monthlyRentFee <= :maxMonthlyRentFee) "
    )
    Page<CrawlingProperty> findByDto(
            @Param("transactionType") TransactionType transactionType,
            @Param("propertyType") PropertyType propertyType,
            @Param("province") String province,
            @Param("city") String city,
            @Param("dong") String dong,
            @Param("minSalePrice") Float minSalePrice,
            @Param("maxSalePrice") Float maxSalePrice,
            @Param("minDeposit") Float minDeposit,
            @Param("maxDeposit") Float maxDeposit,
            @Param("minMonthlyRentFee") Float minMonthlyRentFee,
            @Param("maxMonthlyRentFee") Float maxMonthlyRentFee,
            Pageable pageable
    );

    // findOne
    Optional<CrawlingProperty> findById(String id);



    // 임시로 만든 검색 코드
    @Query(
            "SELECT cp " +
                    "FROM CrawlingProperty cp " +
                    "WHERE (:transactionType IS NULL OR cp.transactionType = :transactionType) " +
                    "AND (:propertyType IS NULL OR cp.propertyType = :propertyType) " +
                    "AND (:province IS NULL OR cp.province = :province) " +
                    "AND (:city IS NULL OR cp.city = :city) " +
                    "AND (:dong IS NULL OR cp.dong LIKE CONCAT('%', :dong, '%')) " +
                    "AND (:minSalePrice IS NULL OR cp.salePrice >= :minSalePrice) " +
                    "AND (:maxSalePrice IS NULL OR cp.salePrice <= :maxSalePrice) " +
                    "AND (:minDeposit IS NULL OR cp.deposit >= :minDeposit) " +
                    "AND (:maxDeposit IS NULL OR cp.deposit <= :maxDeposit) " +
                    "AND (:minMonthlyRentFee IS NULL OR cp.monthlyRentFee >= :minMonthlyRentFee) " +
                    "AND (:maxMonthlyRentFee IS NULL OR cp.monthlyRentFee <= :maxMonthlyRentFee) "
    )
    List<CrawlingProperty> findByDto2(
            @Param("transactionType") TransactionType transactionType,
            @Param("propertyType") PropertyType propertyType,
            @Param("province") String province,
            @Param("city") String city,
            @Param("dong") String dong,
            @Param("minSalePrice") Float minSalePrice,
            @Param("maxSalePrice") Float maxSalePrice,
            @Param("minDeposit") Float minDeposit,
            @Param("maxDeposit") Float maxDeposit,
            @Param("minMonthlyRentFee") Float minMonthlyRentFee,
            @Param("maxMonthlyRentFee") Float maxMonthlyRentFee
    );
}