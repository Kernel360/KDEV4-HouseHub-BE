package com.househub.backend.domain.crawlingProperty.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyReqDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyTagListResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyTagResDto;
import com.househub.backend.domain.crawlingProperty.service.CrawlingPropertyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
		log.info("크롤링 매물 목록 조회 요청 - crawlingPropertyReqDto={}", crawlingPropertyReqDto.toString());
		// page를 1-based에서 0-based로 변경
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int size = pageable.getPageSize();

		Pageable adjustedPageable = PageRequest.of(page, size, pageable.getSort());

		CrawlingPropertyTagListResDto response = crawlingPropertyService.findAll(crawlingPropertyReqDto,
			adjustedPageable);
		return ResponseEntity.ok(
			SuccessResponse.success("크롤링 매물 목록 조회에 성공했습니다.", "FIND_CRAWLING_PROPERTY_SUCCESS", response));
	}

	@GetMapping("/{id}")
	public ResponseEntity<SuccessResponse<CrawlingPropertyTagResDto>> findOneCrawlingProperty(
		@PathVariable("id") String id
	) {
		CrawlingPropertyTagResDto response = crawlingPropertyService.findOne(id);
		return ResponseEntity.ok(
			SuccessResponse.success("크롤링 매물 상세 조회에 성공했습니다.", "FIND_CRAWLING_PROPERTY_SUCCESS", response));
	}
}
