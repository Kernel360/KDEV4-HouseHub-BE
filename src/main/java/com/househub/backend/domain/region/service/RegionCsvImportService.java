package com.househub.backend.domain.region.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.domain.region.entity.Region;
import com.househub.backend.domain.region.enums.RegionLevel;
import com.househub.backend.domain.region.repository.RegionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegionCsvImportService {

	private final RegionRepository regionRepository;
	private final Map<String, String> regionCodeMap = new ConcurrentHashMap<>();

	@Transactional
	public void loadRegionData(MultipartFile file) throws IOException {
		try (BufferedReader br = new BufferedReader(
			new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

			// 헤더 스킵
			br.readLine();

			String line;

			// 이미 존재하는 code들을 가져와서 중복 삽입을 방지
			List<String> existingCodes = regionRepository.findAllCodes(); // SELECT code FROM region;
			Set<String> existingCodeSet = new HashSet<>(existingCodes);

			List<Region> regions = new ArrayList<>();
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",", -1); // 빈 문자열 포함

				String code = tokens[0].trim();
				if (existingCodeSet.contains(code)) {
					continue;
				}

				String province = tokens[1].trim();
				String city = tokens[2].trim();
				String dong = tokens[3].trim();
				String name;

				// 계층형 RegionLevel 설정
				RegionLevel level;
				String parentCode = null;

				if (!province.isEmpty() && city.isEmpty() && dong.isEmpty()) {
					// 시도(DO)
					level = RegionLevel.DO;
					name = province;
					parentCode = null; // 시도의 상위 행정코드는 없음
					regionCodeMap.put(province, code); // 시도의 코드와 이름을 맵에 저장
				} else if (!province.isEmpty() && !city.isEmpty() && dong.isEmpty()) {
					// 시군구(SIGUNGU)
					level = RegionLevel.SIGUNGU;
					name = city;
					parentCode = regionCodeMap.get(province); // 시군구의 상위 행정코드는 시도
					regionCodeMap.put(city, code); // 시도의 코드와 이름을 맵에 저장
				} else {
					// 읍면동(DONG)
					level = RegionLevel.DONG;
					name = dong;
					parentCode = regionCodeMap.get(city); // 읍면동의 상위 행정코드는 시군구
				}

				// 새로운 Region 엔티티 생성
				Region region = Region.builder()
					.code(code)
					.name(name)
					.level(level)
					.parentCode(parentCode)
					.build();

				regions.add(region);
			}

			// 데이터베이스에 저장
			regionRepository.saveAll(regions);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
