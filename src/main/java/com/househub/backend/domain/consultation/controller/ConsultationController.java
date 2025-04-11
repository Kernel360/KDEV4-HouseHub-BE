package com.househub.backend.domain.consultation.controller;

import java.time.LocalDate;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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
import com.househub.backend.common.util.SecurityUtil;
import com.househub.backend.domain.consultation.dto.ConsultationListResDto;
import com.househub.backend.domain.consultation.dto.ConsultationReqDto;
import com.househub.backend.domain.consultation.dto.ConsultationResDto;
import com.househub.backend.domain.consultation.enums.ConsultationStatus;
import com.househub.backend.domain.consultation.enums.ConsultationType;
import com.househub.backend.domain.consultation.service.ConsultationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
public class ConsultationController {

	private final ConsultationService consultationService;

	@PostMapping("")
	public ResponseEntity<SuccessResponse<ConsultationResDto>> createConsultation(
		@Valid @RequestBody ConsultationReqDto consultationReqDto
	) {
		Long agentId = SecurityUtil.getAuthenticatedAgent().getId();
		ConsultationResDto response = consultationService.create(consultationReqDto, agentId);
		return ResponseEntity.ok(SuccessResponse.success("상담 등록이 완료되었습니다.", "CONSULTATION_REGISTER_SUCCESS", response));
	}

	@GetMapping("")
	public ResponseEntity<SuccessResponse<ConsultationListResDto>> findAllConsultations(
		@RequestParam(required = false) String keyword,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
		@RequestParam(required = false) ConsultationType type, // 'PHONE', 'VISIT'
		@RequestParam(required = false) ConsultationStatus status, // 'RESERVED', 'COMPLETED', 'CANCELED'
		@RequestParam(required = false, defaultValue = "consultationDate") String sortBy,
		@RequestParam(required = false, defaultValue = "desc") String sortDirection,
		@PageableDefault(size = 10, sort = "consultationDate", direction = Sort.Direction.DESC) Pageable pageable
	) {
		// 💡 page를 1-based에서 0-based로 변경
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int size = pageable.getPageSize();

		Pageable adjustedPageable = PageRequest.of(page, size,
			Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
		Long agentId = SecurityUtil.getAuthenticatedAgent().getId();

		ConsultationListResDto response = consultationService.findAll(agentId, keyword, startDate, endDate,
			type, status, adjustedPageable);
		return ResponseEntity.ok(
			SuccessResponse.success("상담 목록 조회에 성공했습니다.", "FIND_ALL_CONSULTATION_SUCCESS", response));
	}

	@GetMapping("/{id}")
	public ResponseEntity<SuccessResponse<ConsultationResDto>> findOneConsultation(@PathVariable Long id) {
		Long agentId = SecurityUtil.getAuthenticatedAgent().getId();
		ConsultationResDto response = consultationService.findOne(id, agentId);
		return ResponseEntity.ok(SuccessResponse.success("상담 상세 조회에 성공했습니다.", "FIND_CONSULTATION_SUCCESS", response));
	}

	@PutMapping("/{id}")
	public ResponseEntity<SuccessResponse<ConsultationResDto>> updateConsultation(
		@Valid @RequestBody ConsultationReqDto consultationReqDto,
		@PathVariable Long id
	) {
		Long agentId = SecurityUtil.getAuthenticatedAgent().getId();
		ConsultationResDto response = consultationService.update(id, consultationReqDto, agentId);
		return ResponseEntity.ok(SuccessResponse.success("상담 정보 수정에 성공했습니다.", "UPDATE_CONSULTATION_SUCCESS", response));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponse<ConsultationResDto>> deleteConsultation(@PathVariable Long id) {
		Long agentId = SecurityUtil.getAuthenticatedAgent().getId();
		ConsultationResDto response = consultationService.delete(id, agentId);
		return ResponseEntity.ok(SuccessResponse.success("상담 정보 삭제에 성공했습니다.", "DELETE_CONSULTATION_SUCCESS", response));
	}
}
