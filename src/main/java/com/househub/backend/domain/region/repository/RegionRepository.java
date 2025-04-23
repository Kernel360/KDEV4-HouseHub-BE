package com.househub.backend.domain.region.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.househub.backend.domain.region.entity.Region;

import io.lettuce.core.dynamic.annotation.Param;

public interface RegionRepository extends JpaRepository<Region, Long> {
	@Query("SELECT r.id FROM Region r")
	List<Long> findAllCodes(); // 모든 지역 코드 조회

	@Query("SELECT DISTINCT r.province FROM Region r WHERE r.province IS NOT NULL AND r.province <> ''")
	List<String> findDistinctProvinces();

	@Query("SELECT DISTINCT r.city FROM Region r WHERE r.province = :province AND r.city IS NOT NULL AND r.city <> ''")
	List<String> findCitiesByProvince(@Param("province") String province);

	@Query("SELECT DISTINCT r.dong FROM Region r WHERE r.province = :province AND r.city = :city AND r.dong IS NOT NULL AND r.dong <> ''")
	List<String> findDongsByProvinceAndCity(@Param("province") String province, @Param("city") String city);
}