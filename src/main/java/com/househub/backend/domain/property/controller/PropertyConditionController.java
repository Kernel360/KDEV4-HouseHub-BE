package com.househub.backend.domain.property.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.property.dto.propertyCondition.PropertyConditionListResDto;
import com.househub.backend.domain.property.dto.propertyCondition.PropertyConditionReqDto;
import com.househub.backend.domain.property.dto.propertyCondition.PropertyConditionUpdateReqDto;
import com.househub.backend.domain.property.service.PropertyConditionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/properties/{propertyId}/conditions")
@RequiredArgsConstructor
public class PropertyConditionController {

	private final PropertyConditionService propertyConditionService;

	// 매물 조건 추가
	@PostMapping
	public ResponseEntity<SuccessResponse<Void>> createProperty(
		@PathVariable("propertyId") Long propertyId,
		@RequestBody @Valid PropertyConditionReqDto propertyConditionReqDto) {
		propertyConditionService.createPropertyCondition(propertyId, propertyConditionReqDto);
		return ResponseEntity.ok(SuccessResponse.success("매물 조건이 성공적으로 등록되었습니다.", "CREATE_PROPERTY_CONDITION_SUCCESS", null));
	}

	// 매물 조건 수정
	@PutMapping("/{id}")
	public ResponseEntity<SuccessResponse<Void>> updateProperty(
		@PathVariable("propertyId") Long propertyId,
		@PathVariable("id") Long id,
		@RequestBody @Valid PropertyConditionUpdateReqDto propertyConditionReqDto) {
		propertyConditionService.updatePropertyCondition(propertyId, id, propertyConditionReqDto);
		return ResponseEntity.ok(SuccessResponse.success("매물 조건이 성공적으로 수정되었습니다.", "UPDATE_PROPERTY_CONDITION_SUCCESS", null));
	}

	// 매물 조건 조회
	@GetMapping
	public ResponseEntity<SuccessResponse<PropertyConditionListResDto>> findPropertyConditions(
		@PathVariable("propertyId") Long propertyId, Pageable pageable) {
		// page를 1-based에서 0-based로 변경
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int size = pageable.getPageSize();
		Pageable adjustedPageable = PageRequest.of(page, size, pageable.getSort());
		PropertyConditionListResDto response = propertyConditionService.findPropertyConditions(propertyId, adjustedPageable);
		return ResponseEntity.ok(SuccessResponse.success("매물 조건 조회 성공", "FIND_PROPERTY_CONDITIONS_SUCCESS", response));
	}

	// 매물 조건 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponse<Void>> deleteProperty(
		@PathVariable("propertyId") Long propertyId,
		@PathVariable("id") Long id) {
		propertyConditionService.deletePropertyCondition(propertyId, id);
		return ResponseEntity.ok(SuccessResponse.success("매물 조건이 성공적으로 삭제되었습니다.", "DELETE_PROPERTY_CONDITION_SUCCESS", null));
	}
}
