package com.househub.backend.domain.region.service;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.househub.backend.domain.region.dto.RegionDto;
import com.househub.backend.domain.region.entity.Region;
import com.househub.backend.domain.region.enums.RegionLevel;
import com.househub.backend.domain.region.repository.RegionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegionCacheInitializer implements CommandLineRunner {
	private final RegionRepository regionRepository;
	private final RedisTemplate<String, Object> redisTemplate;

	private static final String REDIS_KEY = "regions:do";

	@Override
	public void run(String... args) {
		// '도' 단위(광역시 포함) Region만 조회
		List<Region> doRegions = regionRepository.findByLevel(RegionLevel.DO)
			.stream()
			.filter(region -> !region.getName().contains("특별") && !region.getName().contains("직할"))
			.toList();
		if (doRegions.isEmpty()) {
			log.warn("❌ DO 지역 목록이 비어있습니다. Redis 캐싱을 건너뜁니다.");
			return;
		}
		log.info("✅ DO 지역 목록을 조회했습니다. (Size: {})", doRegions.size());
		log.info("{}", !"서울특별시".contains("특별시"));

		// Redis 저장할 DTO 변환
		List<RegionDto> regionDtos = doRegions.stream()
			.map(RegionDto::fromEntity)
			.toList();

		// Redis 저장
		redisTemplate.opsForValue().set(REDIS_KEY, regionDtos);

		log.info("✅ DO 지역 목록이 Redis에 캐싱되었습니다. (Key: {} ) ", REDIS_KEY);
	}
}
