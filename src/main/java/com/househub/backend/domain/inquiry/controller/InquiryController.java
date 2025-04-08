package com.househub.backend.domain.inquiry.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.common.util.SecurityUtil;
import com.househub.backend.domain.inquiry.dto.CreateInquiryReqDto;
import com.househub.backend.domain.inquiry.dto.CreateInquiryResDto;
import com.househub.backend.domain.inquiry.dto.InquiryListResDto;
import com.househub.backend.domain.inquiry.service.InquiryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class InquiryController {
	private final InquiryService inquiryService;

	/**
	 * 고객 문의를 등록합니다.
	 *
	 * @param reqDto 문의 등록 요청
	 * @return 생성된 문의 응답
	 */
	@Operation(summary = "문의 등록", description = "고객이 남긴 문의를 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "문의 등록 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
	})
	@PostMapping
	public ResponseEntity<SuccessResponse<CreateInquiryResDto>> createInquiry(
		@Valid @RequestBody CreateInquiryReqDto reqDto
	) {
		CreateInquiryResDto response = inquiryService.createInquiry(reqDto);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(SuccessResponse.success("문의가 성공적으로 등록되었습니다", "INQUIRY_CREATE_SUCCESS", response));
	}

	/**
	 * 에이전트가 등록한 문의 목록을 조회합니다.
	 *
	 * @param keyword 검색어
	 * @param pageable 페이지 정보
	 * @return 문의 목록 응답
	 */
	@Operation(summary = "문의 목록 조회", description = "에이전트가 등록한 문의 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "문의 목록 조회 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
	})
	@GetMapping
	public ResponseEntity<SuccessResponse<InquiryListResDto>> getInquiries(
		@RequestParam(required = false, defaultValue = "")
		String keyword,
		@PageableDefault(size = 10) Pageable pageable
	) {
		// 💡 page를 1-based에서 0-based로 변경
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int size = pageable.getPageSize();

		Pageable adjustedPageable = PageRequest.of(page, size, pageable.getSort());

		InquiryListResDto response = inquiryService.getInquiries(getSignInAgentId(), keyword, adjustedPageable);
		return ResponseEntity.ok(SuccessResponse.success(
			"문의 목록 조회 성공",
			"INQUIRY_LIST_SUCCESS",
			response));
	}

	/**
	 * 현재 로그인한 에이전트의 ID를 반환합니다.
	 *
	 * @return 현재 로그인한 에이전트의 ID
	 */
	private Long getSignInAgentId() {
		return SecurityUtil.getAuthenticatedAgent().getId();
	}
}
