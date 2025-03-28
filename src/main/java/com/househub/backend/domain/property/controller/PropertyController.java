package com.househub.backend.domain.property.controller;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.property.dto.*;
import com.househub.backend.domain.property.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    // 매물 등록
    @PostMapping
    public ResponseEntity<SuccessResponse<CreatePropertyResDto>> createProperty(@RequestBody @Valid PropertyReqDto createPropertyDto) {
        CreatePropertyResDto response = propertyService.createProperty(createPropertyDto);
        return ResponseEntity.ok(SuccessResponse.success("매물이 성공적으로 등록되었습니다.", "CREATE_PROPERTY_SUCCESS", response));
    }

    // 전체 매물 조회
    @GetMapping
    public ResponseEntity<SuccessResponse<List<FindPropertyResDto>>> findProperties(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        List<FindPropertyResDto> response = propertyService.findProperties(page, size);
        return ResponseEntity.ok(SuccessResponse.success("매물 조회 성공", "FIND_PROPERTY_SUCCESS", response));
    }


    // 매물 상세 조회
    @GetMapping("/{propertyId}")
    public ResponseEntity<SuccessResponse<FindPropertyDetailResDto>> findProperty(@PathVariable("propertyId") Long propertyId) {
        FindPropertyDetailResDto response = propertyService.findProperty(propertyId);
        return ResponseEntity.ok(SuccessResponse.success("매물 상세 조회 성공", "FIND_DETAIL_PROPERTY_SUCCESS", response));
    }


    // 매물 정보 수정
    @PutMapping("/{propertyId}")
    public ResponseEntity<SuccessResponse<Void>> updateProperty(
            @PathVariable("propertyId") Long propertyId,
            @RequestBody @Valid PropertyReqDto updatePropertyReqDto
    ) {
        propertyService.updateProperty(propertyId, updatePropertyReqDto);
        return ResponseEntity.ok(SuccessResponse.success("매물이 성공적으로 수정되었습니다.", "UPDATE_PROPERTY_SUCCESS", null));
    }


    // 매물 삭제
    @DeleteMapping("/{propertyId}")
    public ResponseEntity<SuccessResponse<Void>> deleteProperty(@PathVariable("propertyId") Long propertyId) {
        propertyService.deleteProperty(propertyId);
        return ResponseEntity.ok(SuccessResponse.success("매물이 성공적으로 삭제되었습니다.", "DELETE_PROPERTY_SUCCESS", null));
    }
}
