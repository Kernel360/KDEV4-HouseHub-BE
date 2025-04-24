package com.househub.backend.domain.property.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.agent.entity.QAgent;
import com.househub.backend.domain.contract.entity.QContract;
import com.househub.backend.domain.customer.entity.QCustomer;
import com.househub.backend.domain.property.dto.PropertySearchDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.entity.QProperty;
import com.househub.backend.domain.property.enums.PropertyType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PropertyRepositoryCustomImpl implements PropertyRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Property> searchProperties(Long agentId, PropertySearchDto searchDto, Pageable pageable) {
		QProperty property = QProperty.property;
		QAgent agent = QAgent.agent;
		QCustomer customer = QCustomer.customer;
		QContract contract = QContract.contract;

		// 기본 쿼리 작성
		JPAQuery<Property> query = queryFactory
			.selectFrom(property)
			.join(property.agent, agent)
			.join(property.customer, customer)
			.leftJoin(property.contracts, contract)  // 계약 정보와 조인
			.where(
				agent.id.eq(agentId),
				provinceEq(searchDto.getProvince()),
				cityEq(searchDto.getCity()),
				dongEq(searchDto.getDong()),
				propertyTypeEq(searchDto.getPropertyType()),
				agentNameContains(searchDto.getAgentName()),
				customerNameContains(searchDto.getCustomerName()),
				activeEq(searchDto.getActive())
			);

		// 거래 유형과 가격 조건 추가
		BooleanBuilder transactionBuilder = new BooleanBuilder();

		if (searchDto.getContractType() != null) {
			transactionBuilder.and(contract.contractType.eq(searchDto.getContractType()));

			// 거래 유형에 따른 가격 필터링
			switch (searchDto.getContractType()) {
				case SALE:  // 매매
					if (searchDto.getMinPrice() != null) {
						transactionBuilder.and(contract.salePrice.goe(searchDto.getMinPrice()));
					}
					if (searchDto.getMaxPrice() != null) {
						transactionBuilder.and(contract.salePrice.loe(searchDto.getMaxPrice()));
					}
					break;

				case JEONSE:  // 전세
					if (searchDto.getMinDeposit() != null) {
						transactionBuilder.and(contract.jeonsePrice.goe(searchDto.getMinDeposit()));
					}
					if (searchDto.getMaxDeposit() != null) {
						transactionBuilder.and(contract.jeonsePrice.loe(searchDto.getMaxDeposit()));
					}
					break;

				case MONTHLY_RENT:  // 월세
					if (searchDto.getMinDeposit() != null) {
						transactionBuilder.and(contract.monthlyRentDeposit.goe(searchDto.getMinDeposit()));
					}
					if (searchDto.getMaxDeposit() != null) {
						transactionBuilder.and(contract.monthlyRentDeposit.loe(searchDto.getMaxDeposit()));
					}
					if (searchDto.getMinMonthlyRent() != null) {
						transactionBuilder.and(contract.monthlyRentFee.goe(searchDto.getMinMonthlyRent()));
					}
					if (searchDto.getMaxMonthlyRent() != null) {
						transactionBuilder.and(contract.monthlyRentFee.loe(searchDto.getMaxMonthlyRent()));
					}
					break;
			}

			// 거래 유형 필터링이 있는 경우 메인 쿼리에 추가
			query.where(transactionBuilder);
		}

		// 정렬 및 페이징
		query.orderBy(customer.createdAt.desc());

		// 전체 개수 조회를 위한 쿼리
		long total = query.fetchCount();

		// 실제 결과 조회
		List<Property> results = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return new PageImpl<>(results, pageable, total);
	}

	// 각 조건에 대한 BooleanExpression 메서드
	private BooleanExpression provinceEq(String province) {
		return province != null ? QProperty.property.province.eq(province) : null;
	}

	private BooleanExpression cityEq(String city) {
		return city != null ? QProperty.property.city.eq(city) : null;
	}

	private BooleanExpression dongEq(String dong) {
		return dong != null ? QProperty.property.dong.eq(dong) : null;
	}

	private BooleanExpression propertyTypeEq(PropertyType propertyType) {
		return propertyType != null ? QProperty.property.propertyType.eq(propertyType) : null;
	}

	private BooleanExpression agentNameContains(String agentName) {
		return agentName != null ? QAgent.agent.name.contains(agentName) : null;
	}

	private BooleanExpression customerNameContains(String customerName) {
		return customerName != null ? QCustomer.customer.name.contains(customerName) : null;
	}

	private BooleanExpression activeEq(Boolean active) {
		return active != null ? QProperty.property.active.eq(active) : null;
	}

}
