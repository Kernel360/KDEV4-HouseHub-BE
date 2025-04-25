package com.househub.backend.domain.region.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.househub.backend.domain.region.dto.RegionOptionDto;
import com.househub.backend.domain.region.entity.Region;

import io.lettuce.core.dynamic.annotation.Param;

public interface RegionRepository extends JpaRepository<Region, Long> {
	@Query("SELECT r.id FROM Region r")
	List<Long> findAllCodes(); // 모든 지역 코드 조회

	// 1. 시/도 조회
	@Query("SELECT new com.househub.backend.domain.region.dto.RegionOptionDto(MIN(r.code), r.province) " +
		"FROM Region r " +
		"WHERE r.province IS NOT NULL AND r.province <> '' " +
		"GROUP BY r.province")
	List<RegionOptionDto> findDistinctProvinces();

	// 2. 시/군/구 조회
	@Query("SELECT new com.househub.backend.domain.region.dto.RegionOptionDto(MIN(r.code), r.city) " +
		"FROM Region r " +
		"WHERE r.province = :province AND r.city IS NOT NULL AND r.city <> '' " +
		"GROUP BY r.city")
	List<RegionOptionDto> findCitiesByProvince(@Param("province") String province);

	// 3. 읍/면/동 조회
	@Query("SELECT new com.househub.backend.domain.region.dto.RegionOptionDto(MIN(r.code), r.dong) " +
		"FROM Region r " +
		"WHERE r.province = :province AND r.city = :city AND r.dong IS NOT NULL AND r.dong <> '' " +
		"GROUP BY r.dong")
	List<RegionOptionDto> findDongsByProvinceAndCity(@Param("province") String province, @Param("city") String city);

}