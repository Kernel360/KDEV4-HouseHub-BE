package com.househub.backend.domain.crawlingProperty.repository;

import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyTagResDto;
import com.househub.backend.domain.crawlingProperty.entity.CrawlingProperty;
import com.househub.backend.domain.crawlingProperty.enums.PropertyType;
import com.househub.backend.domain.crawlingProperty.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrawlingPropertyRepositoryCustom {

    Page<CrawlingPropertyTagResDto> findByTags(List<String> propertyIds, List<Long> tagIds, Pageable pageable);
    Page<CrawlingProperty> findAllWithTags(
            TransactionType transactionType,
            PropertyType propertyType,
            String province,
            String city,
            String dong,
            Float minSalePrice,
            Float maxSalePrice,
            Float minDeposit,
            Float maxDeposit,
            Float minMonthlyRentFee,
            Float maxMonthlyRentFee,
            Pageable pageable
    );
}
