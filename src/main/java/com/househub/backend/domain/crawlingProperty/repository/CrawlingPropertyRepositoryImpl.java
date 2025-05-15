package com.househub.backend.domain.crawlingProperty.repository;

import static com.househub.backend.domain.crawlingProperty.entity.QCrawlingProperty.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyTagResDto;
import com.househub.backend.domain.crawlingProperty.entity.CrawlingProperty;
import com.househub.backend.domain.crawlingProperty.entity.CrawlingPropertyTagMap;
import com.househub.backend.domain.crawlingProperty.entity.QCrawlingProperty;
import com.househub.backend.domain.crawlingProperty.entity.QCrawlingPropertyTagMap;
import com.househub.backend.domain.crawlingProperty.enums.PropertyType;
import com.househub.backend.domain.crawlingProperty.enums.TransactionType;
import com.househub.backend.domain.tag.entity.QTag;
import com.househub.backend.domain.tag.entity.Tag;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

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
		return new PageImpl<>(dtoList, pageable, dtoList.size());
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
		List<Long> tagIds,
		Pageable pageable
	) {
		QCrawlingProperty cp = crawlingProperty;
		QCrawlingPropertyTagMap cptm = QCrawlingPropertyTagMap.crawlingPropertyTagMap;
		QTag tag = QTag.tag;

		BooleanBuilder baseConditions = buildConditions(transactionType, propertyType, province, city, dong,
			minSalePrice, maxSalePrice, minDeposit, maxDeposit, minMonthlyRentFee, maxMonthlyRentFee);

		// tagIds가 비어있지 않으면 tag 조건 추가
		BooleanBuilder tagCondition = new BooleanBuilder();
		boolean hasTagFilter = tagIds != null && !tagIds.isEmpty();
		if (hasTagFilter) {
			tagCondition.and(tag.tagId.in(tagIds));
		}

		// 1단계: ID 조회 쿼리
		JPAQuery<String> idQuery = queryFactory.select(cp.crawlingPropertiesId)
			.from(cp);

		if (hasTagFilter) {
			idQuery.leftJoin(cp.crawlingPropertyTagMaps, cptm)
				.leftJoin(cptm.tag, tag)
				.where(baseConditions.and(tagCondition))
				.groupBy(cp.crawlingPropertiesId)
				.orderBy(Expressions.numberTemplate(Long.class, "count({0})", tag.tagId).desc());
		} else {
			idQuery.where(baseConditions);
		}

		idQuery.offset(pageable.getOffset())
			.limit(pageable.getPageSize());

		List<String> propertyIds = idQuery.fetch();

		if (propertyIds.isEmpty()) {
			return Page.empty(pageable);
		}

		// 2단계: ID 목록 기준으로 엔티티 fetch join 로딩
		List<CrawlingProperty> contents = queryFactory
			.selectDistinct(cp)
			.from(cp)
			.leftJoin(cp.crawlingPropertyTagMaps, cptm).fetchJoin()
			.leftJoin(cptm.tag, tag).fetchJoin()
			.where(cp.crawlingPropertiesId.in(propertyIds))
			.fetch();

		// 3단계: total count 쿼리도 tag 필터 조건 반영
		JPAQuery<Long> countQuery = queryFactory.select(cp.count())
			.from(cp);

		if (hasTagFilter) {
			countQuery.leftJoin(cp.crawlingPropertyTagMaps, cptm)
				.leftJoin(cptm.tag, tag)
				.where(baseConditions.and(tagCondition))
				.groupBy(cp.crawlingPropertiesId);

			// 총 개수를 구하기 위해 그룹핑 없이 countDistinct로 변경
			Long totalCount;

			if (hasTagFilter) {
				totalCount = queryFactory
					.select(cp.countDistinct())
					.from(cp)
					.leftJoin(cp.crawlingPropertyTagMaps, cptm)
					.leftJoin(cptm.tag, tag)
					.where(baseConditions.and(tagCondition))
					.fetchOne();
			} else {
				totalCount = queryFactory
					.select(cp.count())
					.from(cp)
					.where(baseConditions)
					.fetchOne();
			}

			if (totalCount == null)
				totalCount = 0L;

			return new PageImpl<>(contents, pageable, totalCount);
		} else {
			countQuery.where(baseConditions);
			Long totalCount = countQuery.fetchOne();
			return new PageImpl<>(contents, pageable, totalCount == null ? 0 : totalCount);
		}
	}

	private BooleanBuilder buildConditions(TransactionType transactionType, PropertyType propertyType, String province,
		String city, String dong, Float minSalePrice, Float maxSalePrice, Float minDeposit, Float maxDeposit,
		Float minMonthlyRentFee, Float maxMonthlyRentFee) {
		BooleanBuilder builder = new BooleanBuilder();
		if (transactionType != null) {
			builder.and(crawlingProperty.transactionType.eq(transactionType));
		}
		if (propertyType != null) {
			builder.and(crawlingProperty.propertyType.eq(propertyType));
		}
		if (province != null) {
			builder.and(crawlingProperty.province.like("%" + province + "%"));
		}
		if (city != null) {
			builder.and(crawlingProperty.city.like("%" + city + "%"));
		}
		if (dong != null) {
			builder.and(crawlingProperty.dong.like("%" + dong + "%"));
		}
		if (minSalePrice != null) {
			builder.and(crawlingProperty.salePrice.goe(minSalePrice));
		}
		if (maxSalePrice != null) {
			builder.and(crawlingProperty.salePrice.loe(maxSalePrice));
		}
		if (minDeposit != null) {
			builder.and(crawlingProperty.deposit.goe(minDeposit));
		}
		if (maxDeposit != null) {
			builder.and(crawlingProperty.deposit.loe(maxDeposit));
		}
		if (minMonthlyRentFee != null) {
			builder.and(crawlingProperty.monthlyRentFee.goe(minMonthlyRentFee));
		}
		if (maxMonthlyRentFee != null) {
			builder.and(crawlingProperty.monthlyRentFee.loe(maxMonthlyRentFee));
		}
		return builder;
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
				province != null ? crawlingProperty.province.contains(province) : null,
				city != null ? crawlingProperty.city.contains(city) : null,
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

	@Override
	public Optional<CrawlingPropertyTagResDto> findCrawlingPropertyById(String crawlingPropertyId) {
		// Q엔티티 명확하게 선언
		QCrawlingProperty cp = crawlingProperty;
		QCrawlingPropertyTagMap cptm = QCrawlingPropertyTagMap.crawlingPropertyTagMap;
		QTag tag = QTag.tag;

		// 1. 매물 조회
		CrawlingProperty property = queryFactory
			.selectFrom(cp)
			.where(cp.crawlingPropertiesId.eq(crawlingPropertyId))
			.fetchOne();

		if (property == null) {
			return Optional.empty();
		}

		// 2. 태그 리스트 조회 (조인)
		List<Tag> tags = queryFactory
			.select(tag)
			.from(cptm)
			.join(cptm.tag, tag)
			.where(cptm.crawlingProperty.crawlingPropertiesId.eq(crawlingPropertyId))
			.fetch();

		// 3. 매물 id와 태그 리스트를 Map에 담아 DTO로 변환
		Map<String, List<Tag>> propertyTagsMap = new HashMap<>();
		propertyTagsMap.put(crawlingPropertyId, tags);

		return Optional.of(CrawlingPropertyTagResDto.fromEntity(property, propertyTagsMap));
	}
}