package com.househub.backend.domain.region.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.domain.region.enums.RegionLevel;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegionCsvImportService {

	private final JdbcTemplate jdbcTemplate;

	public void loadRegionDataFast(MultipartFile file) throws IOException {
		List<Region> regions = parseCsv(file);
		batchInsert(regions);
	}

	private List<Region> parseCsv(MultipartFile file) throws IOException {
		List<Region> regions = new ArrayList<>();
		Map<String, String> regionCodeMap = new HashMap<>();

		try (BufferedReader br = new BufferedReader(
			new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

			br.readLine(); // skip header
			String line;

			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(",", -1);

				String code = tokens[0].trim();
				String province = tokens[1].trim();
				String city = tokens[2].trim();
				String dong = tokens[3].trim();
				String name;
				RegionLevel level;
				String parentCode = null;

				if (!province.isEmpty() && city.isEmpty() && dong.isEmpty()) {
					level = RegionLevel.DO;
					name = province;
					regionCodeMap.put(province, code);
				} else if (!province.isEmpty() && !city.isEmpty() && dong.isEmpty()) {
					level = RegionLevel.SIGUNGU;
					name = city;
					parentCode = regionCodeMap.get(province);
					regionCodeMap.put(city, code);
				} else {
					level = RegionLevel.DONG;
					name = dong;
					parentCode = regionCodeMap.get(city);
				}

				regions.add(new Region(code, name, level.name(), parentCode));
			}
		}

		return regions;
	}

	private void batchInsert(List<Region> regions) {
		String sql = "INSERT INTO region (code, name, level, parent_code) VALUES (?, ?, ?, ?)";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Region r = regions.get(i);
				ps.setString(1, r.getCode());
				ps.setString(2, r.getName());
				ps.setString(3, r.getLevel());
				ps.setString(4, r.getParentCode());
			}

			@Override
			public int getBatchSize() {
				return regions.size();
			}
		});
	}

	// Region DTO class for insert only
	static class Region {
		private final String code;
		private final String name;
		private final String level;
		private final String parentCode;

		public Region(String code, String name, String level, String parentCode) {
			this.code = code;
			this.name = name;
			this.level = level;
			this.parentCode = parentCode;
		}

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public String getLevel() {
			return level;
		}

		public String getParentCode() {
			return parentCode;
		}
	}
}
