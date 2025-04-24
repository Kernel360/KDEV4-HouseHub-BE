package com.househub.backend.domain.region.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.region.dto.RegionOptionDto;
import com.househub.backend.domain.region.service.RegionCsvImportService;
import com.househub.backend.domain.region.service.RegionReadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
@Tag(name = "Region", description = "지역 데이터 관련 API")
public class RegionController {
	private final RegionCsvImportService regionCsvImportService;
	private final RegionReadService regionReadService;

	@Operation(summary = "CSV 파일 업로드로 지역 데이터 저장", description = "CSV 형식의 지역 데이터를 업로드 받아 Region 테이블에 저장합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "업로드 성공"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@PostMapping(value = "/load", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadRegionCsv(
		@Parameter(description = "업로드할 지역 CSV 파일", required = true)
		@RequestParam("file") MultipartFile file
	) {
		try {
			regionCsvImportService.loadRegionData(file);
			return ResponseEntity.ok("지역 데이터가 성공적으로 업로드되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("지역 데이터 업로드 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	@Operation(summary = "도/특별시/광역시 목록 조회")
	@GetMapping("/provinces")
	public ResponseEntity<SuccessResponse<List<RegionOptionDto>>> getProvinces() {
		List<RegionOptionDto> provinces = regionReadService.getProvinces();
		return ResponseEntity.ok(
			SuccessResponse.success("도/특별시/광역시 목록을 성공적으로 조회했습니다.", "FIND_PROVINCES_SUCCESS", provinces));
	}

	@Operation(summary = "시/군/구 목록 조회", parameters = {
		@Parameter(name = "province", description = "도/특별시/광역시 이름", required = true)
	})
	@GetMapping("/cities")
	public ResponseEntity<SuccessResponse<List<RegionOptionDto>>> getCities(@RequestParam String province) {
		List<RegionOptionDto> cities = regionReadService.getCities(province);
		return ResponseEntity.ok(SuccessResponse.success("시/군/구 목록을 성공적으로 조회했습니다.", "FIND_CITIES_SUCCESS", cities));
	}

	@Operation(summary = "읍/면/동 목록 조회", parameters = {
		@Parameter(name = "province", description = "도/특별시/광역시 이름", required = true),
		@Parameter(name = "city", description = "시/군/구 이름", required = true)
	})
	@GetMapping("/dongs")
	public ResponseEntity<SuccessResponse<List<RegionOptionDto>>> getDongs(
		@RequestParam String province,
		@RequestParam String city
	) {
		List<RegionOptionDto> dongs = regionReadService.getDongs(province, city);
		return ResponseEntity.ok(SuccessResponse.success("읍/면/동 목록을 성공적으로 조회했습니다.", "FIND_DONGS_SUCCESS", dongs));
	}

}
