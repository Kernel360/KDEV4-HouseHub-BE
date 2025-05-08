package com.househub.backend.domain.region.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.househub.backend.domain.region.dto.RegionDto;
import com.househub.backend.domain.region.entity.Region;
import com.househub.backend.domain.region.enums.RegionLevel;
import com.househub.backend.domain.region.repository.RegionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegionReadService {
	private final RegionRepository regionRepository;

	@Cacheable(value = "regions:do", key = "'regions:do'")
	public List<RegionDto> getDoList() {
		return regionRepository.findByLevel(RegionLevel.DO)
			.stream()
			.filter(region -> !region.getName().contains("특별") && !region.getName().contains("직할"))
			.map(RegionDto::fromEntity)
			.toList();
	}

	@Cacheable(value = "regions:sigungu", key = "#doCode", unless = "#result.isEmpty()")
	public List<RegionDto> getSigunguList(String doCode) {
		List<Region> sigunguList = regionRepository.findByParentCode(doCode);

		return sigunguList
			.stream()
			.map(RegionDto::fromEntity)
			.toList();
	}

	@Cacheable(value = "regions:dong", key = "#sigunguCode", unless = "#result.isEmpty()")
	public List<RegionDto> getDongList(String sigunguCode) {
		return regionRepository.findByParentCode(sigunguCode)
			.stream()
			.map(RegionDto::fromEntity)
			.toList();
	}
}

