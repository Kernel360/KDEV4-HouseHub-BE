package com.househub.backend.domain.region.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.domain.region.entity.Region;
import com.househub.backend.domain.region.repository.RegionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegionCsvImportService {

	private final RegionRepository regionRepository;

	@Transactional
	public void loadRegionData(MultipartFile file) throws IOException {
		try (BufferedReader br = new BufferedReader(
			new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

			// 헤더 스킵
			br.readLine();

			String line;

			List<Long> existingCodes = regionRepository.findAllCodes(); // SELECT code FROM region;
			Set<Long> existingCodeSet = new HashSet<>(existingCodes);

			List<Region> regions = new ArrayList<>();
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",", -1); // 빈 문자열 포함

				Long code = Long.parseLong(tokens[0].trim());
				if (existingCodeSet.contains(code)) {
					continue;
				}

				String province = tokens[1].trim();
				String city = tokens[2].trim();
				String dong = tokens[3].trim();

				String name = String.join(" ",
					!province.isEmpty() ? province : "",
					!city.isEmpty() ? city : "",
					!dong.isEmpty() ? dong : "").trim();

				Region region = Region.builder()
					.code(code)
					.province(province)
					.city(city)
					.dong(dong)
					.name(name)
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

