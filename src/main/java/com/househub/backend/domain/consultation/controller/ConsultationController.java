package com.househub.backend.domain.consultation.controller;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.common.util.SecurityUtil;
import com.househub.backend.domain.consultation.dto.ConsultationReqDto;
import com.househub.backend.domain.consultation.dto.ConsultationResDto;
import com.househub.backend.domain.consultation.service.ConsultationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/consultations")
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
    public ResponseEntity<SuccessResponse<List<ConsultationResDto>>> findAllConsultations() {
        Long agentId = SecurityUtil.getAuthenticatedAgent().getId();
        List<ConsultationResDto> response = consultationService.findAll(agentId);
        return ResponseEntity.ok(SuccessResponse.success("상담 목록 조회에 성공했습니다.", "FIND_ALL_CONSULTATION_SUCCESS", response));
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
