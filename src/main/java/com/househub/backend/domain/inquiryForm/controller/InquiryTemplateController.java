package com.househub.backend.domain.inquiryForm.controller;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.inquiryForm.dto.CreateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryForm.service.InquiryTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inquiry-templates")
@RequiredArgsConstructor
public class InquiryTemplateController {
    private final InquiryTemplateService inquiryTemplateService;

    @PostMapping("")
    public ResponseEntity<SuccessResponse<Void>> createNewInquiryTemplate(@Valid @RequestBody CreateInquiryTemplateReqDto reqDto) {
        inquiryTemplateService.createNewInquiryTemplate(reqDto);
        return ResponseEntity.ok(SuccessResponse.success("새로운 문의 템플릿 등록 성공", "CREATE_NEW_INQUIRY_TEMPLATE_SUCCESS", null));
    }
}
