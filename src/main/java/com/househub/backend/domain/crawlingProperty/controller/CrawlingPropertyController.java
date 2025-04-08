package com.househub.backend.domain.crawlingProperty.controller;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.common.util.SecurityUtil;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyReqDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyResDto;
import com.househub.backend.domain.crawlingProperty.service.CrawlingPropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/crawlingProperties")
public class CrawlingPropertyController {

    private final CrawlingPropertyService crawlingPropertyService;

    @GetMapping("/")
    public ResponseEntity<SuccessResponse<List<CrawlingPropertyResDto>>> findAllCrawlingProperty(
        @ModelAttribute CrawlingPropertyReqDto crawlingPropertyReqDto
    ) {
        List<CrawlingPropertyResDto> response = crawlingPropertyService.findAll(crawlingPropertyReqDto);
        return ResponseEntity.ok(SuccessResponse.success("크롤링 매물 목록 조회에 성공했습니다.", "FIND_CRAWLING_PROPERTY_SUCCESS", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<CrawlingPropertyResDto>> findOneCrawlingProperty(
        @PathVariable String id
    ) {
        CrawlingPropertyResDto response = crawlingPropertyService.findOne(id);
        return ResponseEntity.ok(SuccessResponse.success("크롤링 매물 상세 조회에 성공했습니다.", "FIND_CRAWLING_PROPERTY_SUCCESS", response));
    }
}
