package com.househub.backend.domain.property.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.dashboard.dto.PropertyTypeCount;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyType;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
	Optional<Property> findByIdAndAgentId(Long propertyId, Long agentId);

	// 주소와 상세주소를 결합한 값으로 중복 체크
	boolean existsByRoadAddressAndDetailAddressAndCustomerId(String roadAddress, String detailAddress, Long customerId);

	// 검색 필터링 (주소, 유형, 고객 이름, 공인중개사 이름, 활성화 여부)
	@Query("SELECT p FROM Property p " +
		"JOIN p.agent a " +
		"JOIN p.customer c " +
		"WHERE a.id = :agentId " +
		"AND (:province IS NULL OR p.province = :province) " +
		"AND (:city IS NULL OR p.city = :city) " +
		"AND (:dong IS NULL OR p.dong = :dong) " +
		"AND (:propertyType IS NULL OR p.propertyType = :propertyType) " +
		"AND (:agentName IS NULL OR a.name LIKE CONCAT('%', :agentName, '%')) " +
		"AND (:customerName IS NULL OR c.name LIKE CONCAT('%', :customerName, '%'))" +
		"AND (:active IS NULL OR p.active = :active) " +
		"ORDER BY c.createdAt DESC")
	Page<Property> searchProperties(
		@Param("agentId") Long agentId,
		@Param("province") String province,
		@Param("city") String city,
		@Param("dong") String dong,
		@Param("propertyType") PropertyType propertyType,
		@Param("agentName") String agentName,
		@Param("customerName") String customerName,
		@Param("active") Boolean active,
		Pageable pageable
	);

	@Query("SELECT COUNT(p) FROM Property p WHERE p.agent.id = :agentId")
	long countByAgentId(@Param("agentId") Long agentId);

	@Query("SELECT p FROM Property p WHERE p.agent.id = :agentId ORDER BY p.createdAt DESC")
	List<Property> findRecentPropertiesByAgentId(@Param("agentId") Long agentId, Pageable pageable);

	@Query("SELECT p.propertyType AS propertyType, COUNT(p) AS count " +
		"FROM Property p " +
		"WHERE p.agent.id = :agentId " +
		"GROUP BY p.propertyType")
	List<PropertyTypeCount> countByTypeAndAgentId(@Param("agentId") Long agentId);
}
