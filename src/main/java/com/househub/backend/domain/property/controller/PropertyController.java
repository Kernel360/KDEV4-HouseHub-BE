package com.househub.backend.domain.property.controller;

import com.househub.backend.domain.property.dto.CreatePropertyDto;
import com.househub.backend.domain.property.dto.ResponseDto;
import com.househub.backend.domain.property.service.impl.PropertyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyServiceImpl propertyService;

    // 매물 등록
    @PostMapping
    public ResponseEntity createProperty(@RequestBody CreatePropertyDto postPropertyDto) {
        ResponseDto.PostResponse response = propertyService.createProperty(postPropertyDto);
        return new ResponseEntity(response, HttpStatus.CREATED);
    }
}
