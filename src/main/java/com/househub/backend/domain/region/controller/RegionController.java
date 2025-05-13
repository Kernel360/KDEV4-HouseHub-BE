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
import com.househub.backend.domain.region.dto.RegionDto;
import com.househub.backend.domain.region.service.RegionCsvImportService;
import com.househub.backend.domain.region.service.RegionReadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
			regionCsvImportService.loadRegionDataFast(file);
			return ResponseEntity.ok("지역 데이터가 성공적으로 업로드되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("지역 데이터 업로드 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	// 2. 도 목록 조회 (코드 기반)
	@Operation(summary = "도/특별시/광역시 목록 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도 목록 조회 성공"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@GetMapping("/dos")
	public ResponseEntity<SuccessResponse<List<RegionDto>>> getDoList() {
		List<RegionDto> doList = regionReadService.getDoList();
		return ResponseEntity.ok(
			SuccessResponse.success("도/광역시 목록을 성공적으로 조회했습니다.", "FIND_DO_LIST_SUCCESS", doList));
	}

	// 3. 시군구 목록 조회 (도 코드 기반)
	@Operation(summary = "시/군/구 목록 조회", parameters = {
		@Parameter(name = "doCode", description = "도/특별시/광역시 코드", required = true)
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "시군구 목록 조회 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@GetMapping("/sigungus")
	public ResponseEntity<SuccessResponse<List<RegionDto>>> getSigunguList(@RequestParam String doCode) {
		log.info("doCode: {}", doCode);
		List<RegionDto> sigunguList = regionReadService.getSigunguList(doCode);
		log.info("sigunguList: {}", sigunguList.size() > 0 ? sigunguList : "No data found");
		return ResponseEntity.ok(
			SuccessResponse.success("시/군/구 목록을 성공적으로 조회했습니다.", "FIND_CITIES_SUCCESS", sigunguList));
	}

	// 4. 읍/면/동 목록 조회 (시군구 코드 기반)
	@Operation(summary = "읍/면/동 목록 조회", parameters = {
		@Parameter(name = "sigunguCode", description = "시/군/구 코드", required = true)
	})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "읍면동 목록 조회 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@GetMapping("/dongs")
	public ResponseEntity<SuccessResponse<List<RegionDto>>> getDongList(
		@RequestParam String sigunguCode
	) {
		List<RegionDto> dongList = regionReadService.getDongList(sigunguCode);
		return ResponseEntity.ok(
			SuccessResponse.success("읍/면/동 목록을 성공적으로 조회했습니다.", "FIND_DONG_LIST_SUCCESS", dongList));
	}
}
