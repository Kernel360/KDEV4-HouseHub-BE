package com.househub.backend.domain.crawlingProperty.repository;

import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyTagListResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyTagResDto;
import com.househub.backend.domain.crawlingProperty.entity.*;
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

@Repository
@RequiredArgsConstructor
public class CrawlingPropertyRepositoryImpl implements CrawlingPropertyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 태그 및 조건 조회
    public Page<CrawlingPropertyTagResDto> findByTags(List<String> propertyIds, List<Long> tagIds, Pageable pageable) {
        QCrawlingProperty cp = QCrawlingProperty.crawlingProperty;
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
}