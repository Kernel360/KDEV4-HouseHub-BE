package com.househub.backend.domain.region.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.househub.backend.domain.region.entity.Region;
import com.househub.backend.domain.region.enums.RegionLevel;

public interface RegionRepository extends JpaRepository<Region, Long> {
	@Query("SELECT r.id FROM Region r")
	List<String> findAllCodes(); // 모든 지역 코드 조회

	List<Region> findByLevelOrderByNameAsc(RegionLevel level); // 특정 지역 레벨에 해당하는 지역 조회

	List<Region> findByParentCodeOrderByNameAsc(String parentCode); // 특정 지역 코드에 해당하는 하위 지역 조회
}