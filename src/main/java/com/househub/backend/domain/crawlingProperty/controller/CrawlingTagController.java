package com.househub.backend.domain.crawlingProperty.controller;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingTagResDto;
import com.househub.backend.domain.crawlingProperty.service.CrawlingTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/crawlingTag")
public class CrawlingTagController {

    private final CrawlingTagService crawlingTagService;

    @GetMapping("")
    public ResponseEntity<SuccessResponse<List<CrawlingTagResDto>>> findAllTags() {
        List<CrawlingTagResDto> response = crawlingTagService.findAll();

        return ResponseEntity.ok(SuccessResponse.success("태그 목록 조회에 성공했습니다.", "FIND_TAG_SUCCESS", response));
    }
}
