package com.househub.backend.domain.customer.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.entity.QCustomer;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryCustomImpl implements CustomerRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Customer> findAllByAgentIdAndFiltersAndDeletedOnly(Long agentId, String name, String contact,
		String email, boolean includeDeleted, Pageable pageable) {

		QCustomer customer = QCustomer.customer;

		BooleanBuilder predicate = new BooleanBuilder();
		predicate.and(customer.agent.id.eq(agentId));

		if (includeDeleted) {
			predicate.and(customer.deletedAt.isNotNull());
		} else {
			predicate.and(customer.deletedAt.isNull());
		}

		// 이름/연락처/이메일 동적 필터 추가
		Predicate filterCond = anyFilter(name, contact, email, customer);
		if (filterCond != null) {
			predicate.and(filterCond);
		}

		// 전체조회 쿼리 (변경 없음)
		List<Customer> results = queryFactory
			.selectFrom(customer)
			.where(predicate)
			.orderBy(customer.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// 안전한 카운트 쿼리 작성법 (NPE 방지)
		Long countResult = queryFactory
			.select(customer.count())
			.from(customer)
			.where(predicate)
			.fetchOne();

		// null 안전 처리
		long count = countResult != null ? countResult : 0L;

		return new PageImpl<>(results, pageable, count);

	}

	private Predicate anyFilter(
		String name, String contact, String email, QCustomer customer
	) {
		BooleanBuilder builder = new BooleanBuilder();

		if (name != null && !name.isEmpty()) {
			builder.or(customer.name.contains(name));
		}

		if (contact != null && !contact.isEmpty()) {
			String normalizedContact = contact.replaceAll("[- ]", "");
			builder.or(
				Expressions.stringTemplate("REPLACE(REPLACE({0}, '-', ''), ' ', '')", customer.contact)
					.contains(normalizedContact)
			);
		}

		if (email != null && !email.isEmpty()) {
			builder.or(customer.email.contains(email));
		}

		return builder.hasValue() ? builder.getValue() : null;
	}

}