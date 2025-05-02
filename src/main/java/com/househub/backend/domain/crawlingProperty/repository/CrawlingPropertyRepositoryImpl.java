package com.househub.backend.domain.crawlingProperty.repository;

import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyTagListResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyTagResDto;
import com.househub.backend.domain.crawlingProperty.entity.*;
import com.househub.backend.domain.crawlingProperty.enums.PropertyType;
import com.househub.backend.domain.crawlingProperty.enums.TransactionType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.querydsl.core.Tuple;

import static com.househub.backend.domain.crawlingProperty.entity.QCrawlingProperty.crawlingProperty;

@Repository
@RequiredArgsConstructor
public class CrawlingPropertyRepositoryImpl implements CrawlingPropertyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 태그 및 조건 조회
    public Page<CrawlingPropertyTagResDto> findByTags(List<String> propertyIds, List<Long> tagIds, Pageable pageable) {
        QCrawlingProperty cp = crawlingProperty;
        QCrawlingPropertyTagMap cptm = QCrawlingPropertyTagMap.crawlingPropertyTagMap;
        QTag tag = QTag.tag;

        List<CrawlingProperty> crawlingProperties;

        // 1. tuple 조회
        List<Tuple> tupleContent = queryFactory
                .select(cp, cptm.count())
                .from(cp)
                .join(cp.crawlingPropertyTagMaps, cptm)
                .join(cptm.tag, tag)
                .where(
                        tag.tagId.in(tagIds),
                        cp.crawlingPropertiesId.in(propertyIds)
                )
                .groupBy(cp.crawlingPropertiesId)
                .orderBy(cptm.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (tupleContent.isEmpty()) {
            return Page.empty(pageable);
        }

        // 2. CrawlingProperty 리스트 추출
        crawlingProperties = tupleContent.stream()
                .map(tuple -> tuple.get(0, CrawlingProperty.class))
                .toList();

        // 3. CrawlingProperty ID 리스트 추출
        List<String> crawlingPropertiesIds = crawlingProperties.stream()
                .map(CrawlingProperty::getCrawlingPropertiesId)
                .toList();

        // 4. 매물 ID로 태그 매핑 조회
        List<CrawlingPropertyTagMap> tagMappings = queryFactory
                .selectFrom(cptm)
                .where(cptm.crawlingProperty.crawlingPropertiesId.in(crawlingPropertiesIds))
                .fetch();

        // 5. 매물별로 태그 리스트 묶기
        Map<String, List<Tag>> propertyTagsMap = tagMappings.stream()
                .collect(Collectors.groupingBy(
                        mapping -> mapping.getCrawlingProperty().getCrawlingPropertiesId(), // 여기 이름 충돌 수정
                        Collectors.mapping(CrawlingPropertyTagMap::getTag, Collectors.toList())
                ));

        // 6. DTO로 변환
        List<CrawlingPropertyTagResDto> dtoList = crawlingProperties.stream()
                .map(crawlingProperty -> CrawlingPropertyTagResDto.fromEntity(crawlingProperty, propertyTagsMap))
                .toList();

        // 7. Page로 변환
        return new PageImpl<>(dtoList, pageable, propertyIds.size());
    }

    public Page<CrawlingProperty> findAllWithTags(
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
    ) {
        QCrawlingProperty cp = crawlingProperty;
        QCrawlingPropertyTagMap cptm = QCrawlingPropertyTagMap.crawlingPropertyTagMap;
        QTag tag = QTag.tag;

        // 1. 조건(where절) 구성
        BooleanBuilder builder = new BooleanBuilder();
        if (transactionType != null) {
            builder.and(cp.transactionType.eq(transactionType));
        }
        if (propertyType != null) {
            builder.and(cp.propertyType.eq(propertyType));
        }
        if (province != null) {
            builder.and(cp.province.eq(province));
        }
        if (city != null) {
            builder.and(cp.city.eq(city));
        }
        if (dong != null) {
            builder.and(cp.dong.like("%" + dong + "%"));
        }
        if (minSalePrice != null) {
            builder.and(cp.salePrice.goe(minSalePrice));
        }
        if (maxSalePrice != null) {
            builder.and(cp.salePrice.loe(maxSalePrice));
        }
        if (minDeposit != null) {
            builder.and(cp.deposit.goe(minDeposit));
        }
        if (maxDeposit != null) {
            builder.and(cp.deposit.loe(maxDeposit));
        }
        if (minMonthlyRentFee != null) {
            builder.and(cp.monthlyRentFee.goe(minMonthlyRentFee));
        }
        if (maxMonthlyRentFee != null) {
            builder.and(cp.monthlyRentFee.loe(maxMonthlyRentFee));
        }

        // 2. 본격 조회
        List<CrawlingProperty> contents = queryFactory
                .selectDistinct(cp)
                .from(cp)
                .leftJoin(cp.crawlingPropertyTagMaps, cptm).fetchJoin()
                .leftJoin(cptm.tag, tag).fetchJoin()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 3. total count 조회
        Long total = queryFactory
                .select(cp.count())
                .from(cp)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(contents, pageable, total == null ? 0 : total);
    }

    @Override
    public List<CrawlingProperty> findByDto(
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
            Float maxMonthlyRentFee
    ) {
        return queryFactory
                .selectFrom(crawlingProperty)
                .where(
                        transactionType != null ? crawlingProperty.transactionType.eq(transactionType) : null,
                        propertyType != null ? crawlingProperty.propertyType.eq(propertyType) : null,
                        province != null ? crawlingProperty.province.eq(province) : null,
                        city != null ? crawlingProperty.city.eq(city) : null,
                        dong != null ? crawlingProperty.dong.contains(dong) : null,
                        minSalePrice != null ? crawlingProperty.salePrice.goe(minSalePrice) : null,
                        maxSalePrice != null ? crawlingProperty.salePrice.loe(maxSalePrice) : null,
                        minDeposit != null ? crawlingProperty.deposit.goe(minDeposit) : null,
                        maxDeposit != null ? crawlingProperty.deposit.loe(maxDeposit) : null,
                        minMonthlyRentFee != null ? crawlingProperty.monthlyRentFee.goe(minMonthlyRentFee) : null,
                        maxMonthlyRentFee != null ? crawlingProperty.monthlyRentFee.loe(maxMonthlyRentFee) : null
                )
                .fetch();
    }
}