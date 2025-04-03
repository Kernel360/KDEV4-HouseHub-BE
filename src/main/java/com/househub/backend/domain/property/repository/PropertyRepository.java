package com.househub.backend.domain.property.repository;

import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
	// 주소와 상세주소를 결합한 값으로 중복 체크
	boolean existsByRoadAddressAndDetailAddress(String roadAddress, String detailAddress);

	// 검색 필터링 (주소, 유형, 고객 이름, 공인중개사 이름)
	@Query("SELECT p FROM Property p " +
		"JOIN p.agent a " +
		"JOIN p.customer c " +
		"WHERE (:province IS NULL OR p.province = :province) " +
		"AND (:city IS NULL OR p.city = :city) " +
		"AND (:dong IS NULL OR p.dong = :dong) " +
		"AND (:propertyType IS NULL OR p.propertyType = :propertyType) " +
		"AND (:agentName IS NULL OR a.name LIKE %:agentName%) " +
		"AND (:customerName IS NULL OR c.name LIKE %:customerName%)")
	Page<Property> searchProperties(
		@Param("province") String province,
		@Param("city") String city,
		@Param("dong") String dong,
		@Param("propertyType") PropertyType propertyType,
		@Param("agentName") String agentName,
		@Param("customerName") String customerName,
		Pageable pageable
	);
}
