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
public interface PropertyRepository extends JpaRepository<Property, Long>, PropertyRepositoryCustom {
	Optional<Property> findByIdAndAgentId(Long propertyId, Long agentId);

	// 주소와 상세주소를 결합한 값으로 중복 체크
	boolean existsByRoadAddressAndDetailAddressAndCustomerId(String roadAddress, String detailAddress, Long customerId);

	@Query("SELECT p FROM Property p " +
		"JOIN p.agent a " +
		"JOIN p.customer c " +
		"WHERE a.id = :agentId " +
		"AND c.id = :customerId " +
		"ORDER BY c.createdAt DESC")
	List<Property> searchPropertiesByCustomer(
		@Param("agentId") Long agentId,
		@Param("customerId") Long customerId);

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
