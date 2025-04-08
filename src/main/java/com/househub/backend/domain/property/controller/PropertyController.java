package com.househub.backend.domain.property.controller;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.common.util.SecurityUtil;
import com.househub.backend.domain.property.dto.*;
import com.househub.backend.domain.property.service.PropertyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

	private final PropertyService propertyService;

	// 매물 등록
	@PostMapping
	public ResponseEntity<SuccessResponse<CreatePropertyResDto>> createProperty(
		@RequestBody @Valid PropertyReqDto createPropertyDto) {
		CreatePropertyResDto response = propertyService.createProperty(createPropertyDto, getSignInAgentId());
		return ResponseEntity.ok(SuccessResponse.success("매물이 성공적으로 등록되었습니다.", "CREATE_PROPERTY_SUCCESS", response));
	}

	// 전체 매물 조회 및 검색
	@GetMapping
	public ResponseEntity<SuccessResponse<PropertyListResDto>> findProperties(
		@ModelAttribute PropertySearchDto searchDto,
		Pageable pageable
	) {
		PropertyListResDto response = propertyService.findProperties(searchDto, pageable);
		return ResponseEntity.ok(SuccessResponse.success("매물 조회 성공", "FIND_PROPERTIES_SUCCESS", response));
	}

	// 매물 상세 조회
	@GetMapping("/{id}")
	public ResponseEntity<SuccessResponse<FindPropertyDetailResDto>> findProperty(@PathVariable("id") Long id) {
		FindPropertyDetailResDto response = propertyService.findProperty(id);
		return ResponseEntity.ok(SuccessResponse.success("매물 상세 조회 성공", "FIND_DETAIL_PROPERTY_SUCCESS", response));
	}

	// 매물 정보 수정
	@PutMapping("/{id}")
	public ResponseEntity<SuccessResponse<Void>> updateProperty(
		@PathVariable("id") Long id,
		@RequestBody @Valid PropertyReqDto updatePropertyReqDto
	) {
		propertyService.updateProperty(id, updatePropertyReqDto);
		return ResponseEntity.ok(SuccessResponse.success("매물이 성공적으로 수정되었습니다.", "UPDATE_PROPERTY_SUCCESS", null));
	}

	// 매물 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponse<Void>> deleteProperty(@PathVariable("id") Long id) {
		propertyService.deleteProperty(id);
		return ResponseEntity.ok(SuccessResponse.success("매물이 성공적으로 삭제되었습니다.", "DELETE_PROPERTY_SUCCESS", null));
	}

	/**
	 * 현재 로그인한 에이전트의 ID 반환
	 * @return 현재 로그인한 에이전트의 ID
	 */
	private Long getSignInAgentId() {
		return SecurityUtil.getAuthenticatedAgent().getId();
	}
}
