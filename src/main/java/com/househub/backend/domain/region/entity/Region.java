package com.househub.backend.domain.region.entity;

import com.househub.backend.domain.region.enums.RegionLevel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "regions")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region {

	@Id
	private String code; // ex: 1101053

	@Column(nullable = false)
	private String name; // ex: 사직동

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RegionLevel level; // DO, SIGUNGU, DONG

	@Column(name = "parent_code")
	private String parentCode; // 상위 행정코드 (ex: 11010)
}

