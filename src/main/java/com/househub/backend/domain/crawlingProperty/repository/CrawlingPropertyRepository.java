package com.househub.backend.domain.crawlingProperty.repository;

import com.househub.backend.domain.crawlingProperty.entity.CrawlingProperty;
import com.househub.backend.domain.crawlingProperty.enums.PropertyType;
import com.househub.backend.domain.crawlingProperty.enums.TransactionType;
import org.springframework.data.repository.query.Param;  // Spring Data JPA의 @Param
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CrawlingPropertyRepository extends JpaRepository<CrawlingProperty, String>, CrawlingPropertyRepositoryCustom {
}