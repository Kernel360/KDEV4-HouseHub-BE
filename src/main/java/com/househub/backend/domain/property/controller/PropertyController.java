package com.househub.backend.domain.property.controller;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.property.dto.CreatePropertyReqDto;
import com.househub.backend.domain.property.dto.CreatePropertyResDto;
import com.househub.backend.domain.property.dto.UpdatePropertyReqDto;
import com.househub.backend.domain.property.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    // 매물 등록
    @PostMapping
    public ResponseEntity<SuccessResponse<CreatePropertyResDto>> createProperty(@RequestBody CreatePropertyReqDto createPropertyDto) {
        CreatePropertyResDto response = propertyService.createProperty(createPropertyDto);
        return ResponseEntity.ok(SuccessResponse.success("매물이 성공적으로 등록되었습니다.", "CREATE_PROPERTY_SUCCESS", response));
    }

    // 전체 매물 조회
    @GetMapping
    public ResponseEntity findProperties() {

        return new ResponseEntity(HttpStatus.OK);
    }


//    // 매물 상세 조회
//    @GetMapping("/{propertyId}")
//    public ResponseEntity findProperty(@PathVariable("propertyId") Long propertyId) {
//        FindPropertyResDto response = propertyService.findProperty(propertyId);
////        if(response == null)
//        return new ResponseEntity(response, HttpStatus.OK);
//    }


    // 매물 정보 수정
    @PutMapping("/{propertyId}")
    public ResponseEntity<SuccessResponse<Void>> updateProperty(
            @PathVariable("propertyId") Long propertyId,
            @RequestBody @Valid UpdatePropertyReqDto updatePropertyReqDto) {
        propertyService.updateProperty(propertyId, updatePropertyReqDto);
        return ResponseEntity.ok(SuccessResponse.success("매물이 성공적으로 수정되었습니다.", "UPDATE_PROPERTY_SUCCESS", null));
    }


    // 매물 삭제
    @DeleteMapping("/{propertyId}")
    public ResponseEntity deleteProperty(@PathVariable("propertyId") Long propertyId) {

        return new ResponseEntity(HttpStatus.OK);
    }
}
