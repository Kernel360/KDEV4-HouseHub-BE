package com.househub.backend.domain.property.repository;

<<<<<<< HEAD
<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> 21bde5a (byungchan, feature: 대시보드 통계 데이터 조회 API 구현 #101)
=======
import java.util.List;

>>>>>>> e670440 (byungchan, feature: 최근 등록된 매물 목록 조회 API 구현 #101)
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
<<<<<<< HEAD
import com.househub.backend.domain.dashboard.dto.PropertyTypeCount;
=======
>>>>>>> 21bde5a (byungchan, feature: 대시보드 통계 데이터 조회 API 구현 #101)
=======
import com.househub.backend.domain.dashboard.dto.PropertyTypeCount;
>>>>>>> 56d088f (byungchan, feature: 매물 유형 차트 데이터 조회 API 구현 및 최근 등록된 매물 조회 시 agentId 추가 #101)
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyType;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
	// 주소와 상세주소를 결합한 값으로 중복 체크
	boolean existsByRoadAddressAndDetailAddress(String roadAddress, String detailAddress);

	// 검색 필터링 (주소, 유형, 고객 이름, 공인중개사 이름, 활성화 여부)
	@Query("SELECT p FROM Property p " +
		"JOIN p.agent a " +
		"JOIN p.customer c " +
		"WHERE (:province IS NULL OR p.province = :province) " +
		"AND (:city IS NULL OR p.city = :city) " +
		"AND (:dong IS NULL OR p.dong = :dong) " +
		"AND (:propertyType IS NULL OR p.propertyType = :propertyType) " +
		"AND (:agentName IS NULL OR a.name LIKE CONCAT('%', :agentName, '%')) " +
		"AND (:customerName IS NULL OR c.name LIKE CONCAT('%', :customerName, '%'))" +
		"AND (:active IS NULL OR p.active = :active) ")
		// "AND (:customerName IS NULL OR c.name LIKE CONCAT('%', :customerName, '%'))")
	Page<Property> searchProperties(
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
<<<<<<< HEAD
<<<<<<< HEAD

	@Query("SELECT p FROM Property p WHERE p.agent.id = :agentId ORDER BY p.createdAt DESC")
	List<Property> findRecentPropertiesByAgentId(@Param("agentId") Long agentId, Pageable pageable);

	@Query("SELECT p.propertyType AS propertyType, COUNT(p) AS count " +
		"FROM Property p " +
		"WHERE p.agent.id = :agentId " +
		"GROUP BY p.propertyType")
	List<PropertyTypeCount> countByTypeAndAgentId(@Param("agentId") Long agentId);

=======
>>>>>>> 21bde5a (byungchan, feature: 대시보드 통계 데이터 조회 API 구현 #101)
=======

	@Query("SELECT p FROM Property p WHERE p.agent.id = :agentId ORDER BY p.createdAt DESC")
	List<Property> findRecentPropertiesByAgentId(@Param("agentId") Long agentId, Pageable pageable);

	@Query("SELECT p.propertyType AS propertyType, COUNT(p) AS count " +
		"FROM Property p " +
		"WHERE p.agent.id = :agentId " +
		"GROUP BY p.propertyType")
	List<PropertyTypeCount> countByTypeAndAgentId(@Param("agentId") Long agentId);

>>>>>>> e670440 (byungchan, feature: 최근 등록된 매물 목록 조회 API 구현 #101)
}
