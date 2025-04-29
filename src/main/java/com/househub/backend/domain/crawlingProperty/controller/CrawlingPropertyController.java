package com.househub.backend.domain.crawlingProperty.controller;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyListResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyReqDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyTagListResDto;
import com.househub.backend.domain.crawlingProperty.entity.CrawlingProperty;
import com.househub.backend.domain.crawlingProperty.service.CrawlingPropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/crawlingProperties")
public class CrawlingPropertyController {

    private final CrawlingPropertyService crawlingPropertyService;

    @GetMapping("")
    public ResponseEntity<SuccessResponse<CrawlingPropertyTagListResDto>> findAllCrawlingProperty(
        @ModelAttribute CrawlingPropertyReqDto crawlingPropertyReqDto,
        Pageable pageable
    ) {
        // page를 1-based에서 0-based로 변경
        int page = Math.max(pageable.getPageNumber() - 1, 0);
        int size = pageable.getPageSize();

        Pageable adjustedPageable = PageRequest.of(page, size, pageable.getSort());

        CrawlingPropertyTagListResDto response = crawlingPropertyService.findAll(crawlingPropertyReqDto, adjustedPageable);
        return ResponseEntity.ok(SuccessResponse.success("크롤링 매물 목록 조회에 성공했습니다.", "FIND_CRAWLING_PROPERTY_SUCCESS", response));
    }

    @GetMapping("/tags")
    public ResponseEntity<SuccessResponse<CrawlingPropertyTagListResDto>> findAllCrawlingPropertyWithTags(
        @RequestParam List<Long> tagIds,
        @ModelAttribute CrawlingPropertyReqDto crawlingPropertyReqDto,
        Pageable pageable
    ) {
        // page를 1-based에서 0-based로 변경
        int page = Math.max(pageable.getPageNumber() - 1, 0);
        int size = pageable.getPageSize();

        Pageable adjustedPageable = PageRequest.of(page, size, pageable.getSort());

        CrawlingPropertyTagListResDto response = crawlingPropertyService.findPropertiesByTags(
            tagIds,
            crawlingPropertyReqDto,
            adjustedPageable
        );
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
