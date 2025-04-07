package com.househub.backend.domain.inquiry.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.inquiry.dto.CreateInquiryReqDto;
import com.househub.backend.domain.inquiry.dto.CreateInquiryResDto;
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
}
