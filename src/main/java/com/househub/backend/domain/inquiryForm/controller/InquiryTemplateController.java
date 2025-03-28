package com.househub.backend.domain.inquiryForm.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.inquiryForm.dto.CreateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryForm.dto.InquiryTemplateListResDto;
import com.househub.backend.domain.inquiryForm.service.InquiryTemplateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inquiry-templates")
@RequiredArgsConstructor
public class InquiryTemplateController {
	private final InquiryTemplateService inquiryTemplateService;

	@PostMapping("")
	public ResponseEntity<SuccessResponse<Void>> createNewInquiryTemplate(
		@Valid @RequestBody CreateInquiryTemplateReqDto reqDto) {
		inquiryTemplateService.createNewInquiryTemplate(reqDto);
		return ResponseEntity.ok(
			SuccessResponse.success("새로운 문의 템플릿 등록 성공", "CREATE_NEW_INQUIRY_TEMPLATE_SUCCESS", null));
	}

	@GetMapping("")
	public ResponseEntity<SuccessResponse<InquiryTemplateListResDto>> findInquiryTemplates(
		@RequestParam(required = false) Boolean isActive,
		Pageable pageable
	) {
		InquiryTemplateListResDto response = inquiryTemplateService.getInquiryTemplates(isActive, pageable);
		return ResponseEntity.ok(SuccessResponse.success("문의 템플릿 목록 조회 성공", "GET_INQUIRY_TEMPLATES_SUCCESS", response));
	}
}
