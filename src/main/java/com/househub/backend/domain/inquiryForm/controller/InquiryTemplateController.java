package com.househub.backend.domain.inquiryForm.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.inquiryForm.dto.CreateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryForm.dto.InquiryTemplateListResDto;
import com.househub.backend.domain.inquiryForm.dto.InquiryTemplatePreviewResDto;
import com.househub.backend.domain.inquiryForm.dto.UpdateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryForm.service.InquiryTemplateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/inquiry-templates")
@RequiredArgsConstructor
public class InquiryTemplateController {
	private final InquiryTemplateService inquiryTemplateService;

	/**
	 * 새로운 문의 템플릿을 생성합니다.
	 *
	 * @param reqDto 생성할 문의 템플릿의 정보를 담고 있는 요청 DTO
	 * @return 생성 성공 메시지를 포함한 응답
	 */
	@Operation(summary = "문의 템플릿 생성", description = "새로운 문의 템플릿을 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "문의 템플릿 생성 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
	})
	@PostMapping("")
	public ResponseEntity<SuccessResponse<Void>> createNewInquiryTemplate(
		@Valid @RequestBody CreateInquiryTemplateReqDto reqDto) {
		inquiryTemplateService.createNewInquiryTemplate(reqDto);
		return ResponseEntity.ok(
			SuccessResponse.success("새로운 문의 템플릿 등록 성공", "CREATE_NEW_INQUIRY_TEMPLATE_SUCCESS", null));
	}

	/**
	 * 문의 템플릿 목록을 조회합니다.
	 *
	 * @param isActive 활성화 여부 필터 (선택 사항)
	 * @param pageable 페이지네이션 정보
	 * @return 문의 템플릿 목록을 포함한 응답
	 */
	@Operation(summary = "문의 템플릿 목록 조회", description = "문의 템플릿 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "문의 템플릿 목록 조회 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
	})
	@GetMapping("")
	public ResponseEntity<SuccessResponse<InquiryTemplateListResDto>> findInquiryTemplates(
		@RequestParam(required = false)
		Boolean isActive,
		Pageable pageable
	) {
		InquiryTemplateListResDto response = inquiryTemplateService.getInquiryTemplates(isActive, pageable);
		return ResponseEntity.ok(SuccessResponse.success("문의 템플릿 목록 조회 성공", "GET_INQUIRY_TEMPLATES_SUCCESS", response));
	}

	/**
	 * 키워드를 사용하여 문의 템플릿을 검색합니다.
	 *
	 * @param keyword  검색할 키워드
	 * @param pageable 페이지네이션 정보
	 * @return 검색 결과를 포함한 응답
	 */
	@Operation(summary = "문의 템플릿 검색", description = "키워드를 사용하여 문의 템플릿을 검색합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "문의 템플릿 검색 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
	})
	@GetMapping("/search")
	public ResponseEntity<SuccessResponse<InquiryTemplateListResDto>> searchInquiryTemplates(
		@RequestParam
		String keyword,
		Pageable pageable
	) {
		InquiryTemplateListResDto response = inquiryTemplateService.searchInquiryTemplates(keyword, pageable);
		return ResponseEntity.ok(SuccessResponse.success("문의 템플릿 검색 성공", "SEARCH_INQUIRY_TEMPLATES_SUCCESS", response));
	}

	/**
	 * 문의 템플릿을 미리보기합니다.
	 *
	 * @param templateId 문의 템플릿 ID
	 * @return 문의 템플릿 미리보기 응답
	 */
	@Operation(summary = "문의 템플릿 미리보기", description = "문의 템플릿을 미리보기합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "문의 템플릿 미리보기 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
	})
	@GetMapping("/{templateId}/preview")
	public ResponseEntity<SuccessResponse<InquiryTemplatePreviewResDto>> previewInquiryTemplate(
		@PathVariable
		@NotNull(message = "템플릿 ID는 필수입니다.")
		@Min(value = 1, message = "템플릿 ID는 1 이상이어야 합니다.")
		Long templateId
	) {
		InquiryTemplatePreviewResDto response = inquiryTemplateService.previewInquiryTemplate(templateId);
		return ResponseEntity.ok(
			SuccessResponse.success("문의 템플릿 미리보기 성공", "PREVIEW_INQUIRY_TEMPLATE_SUCCESS", response));
	}

	/**
	 * 문의 템플릿을 수정합니다.
	 * @param templateId 수정할 문의 템플릿의 ID
	 * @param reqDto 수정할 문의 템플릿의 정보를 담고 있는 요청 DTO
	 *               (수정할 필드만 포함하고 있어야 함)
	 *               (수정할 필드가 없는 경우, 빈 객체로 요청)
	 * @returns 수정 성공 메시지를 포함한 응답
	 */
	@Operation(summary = "문의 템플릿 수정", description = "문의 템플릿을 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "문의 템플릿 수정 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
	})
	@PutMapping("/{templateId}")
	public ResponseEntity<SuccessResponse<Void>> updateInquiryTemplate(
		@PathVariable
		@NotNull(message = "템플릿 ID는 필수입니다.")
		@Min(value = 1, message = "템플릿 ID는 1 이상이어야 합니다.")
		Long templateId,
		@Valid
		@RequestBody
		UpdateInquiryTemplateReqDto reqDto
	) {
		inquiryTemplateService.updateInquiryTemplate(templateId, reqDto);
		return ResponseEntity.ok(
			SuccessResponse.success("문의 템플릿 수정 성공", "UPDATE_INQUIRY_TEMPLATE_SUCCESS", null));
	}

	/**
	 * 문의 템플릿을 삭제합니다.
	 * @param templateId 삭제할 문의 템플릿의 ID
	 * @returns 삭제 성공 메시지를 포함한 응답
	 */
	@Operation(summary = "문의 템플릿 삭제", description = "문의 템플릿을 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "문의 템플릿 삭제 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
	})
	@DeleteMapping("/{templateId}")
	public ResponseEntity<SuccessResponse<Void>> deleteInquiryTemplate(
		@PathVariable
		@NotNull(message = "템플릿 ID는 필수입니다.")
		@Min(value = 1, message = "템플릿 ID는 1 이상이어야 합니다.")
		Long templateId
	) {
		inquiryTemplateService.deleteInquiryTemplate(templateId);
		return ResponseEntity.ok(
			SuccessResponse.success("문의 템플릿 삭제 성공", "DELETE_INQUIRY_TEMPLATE_SUCCESS", null));
	}
}
