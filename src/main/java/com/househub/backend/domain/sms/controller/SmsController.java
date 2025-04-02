package com.househub.backend.domain.sms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.common.util.SecurityUtil;
import com.househub.backend.domain.sms.dto.CreateUpdateTemplateReqDto;
import com.househub.backend.domain.sms.dto.GetSmsHistoryReqDto;
import com.househub.backend.domain.sms.dto.SendSmsReqDto;
import com.househub.backend.domain.sms.dto.SmsHistoryResDto;
import com.househub.backend.domain.sms.dto.SmsResDto;
import com.househub.backend.domain.sms.dto.TemplateResDto;
import com.househub.backend.domain.sms.entity.SmsTemplate;
import com.househub.backend.domain.sms.service.SmsService;
import com.househub.backend.domain.sms.service.TemplateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sms")
public class SmsController {

	private final SmsService smsService;
	private final TemplateService templateService;

	/**
	 * SMS 전송 API 엔드포인트
	 * 문자 발송 예약
	 * @param sendSmsReqDto 클라이언트로부터 전달받은 SMS 요청 데이터 (JSON 형식)
	 * @return Aligo API 응답 (JSON 형식)
	 */
	@PostMapping("/send")
	public ResponseEntity<SuccessResponse<SmsResDto>> sendSms(@RequestBody @Valid SendSmsReqDto sendSmsReqDto) {
		SmsResDto response = smsService.sendSms(sendSmsReqDto);
		return ResponseEntity.ok(SuccessResponse.success("문자 전송에 성공했습니다.", "SMS_SEND_SUCCESS", response));
	}

    // 문자 발송 이력 조회
	@GetMapping("/history")
	public ResponseEntity<SuccessResponse<List<SmsHistoryResDto.HistoryDetailDto>>> getSmsHistory(
		@ModelAttribute @Valid GetSmsHistoryReqDto dto
	) {
		List<SmsHistoryResDto.HistoryDetailDto> response = smsService.getRecentMessages(dto.getPage(), dto.getSize(), dto.getStartDate(), dto.getLimitDay());
		return ResponseEntity.ok(SuccessResponse.success("문자 내역 조회에 성공했습니다.", "SMS_HISTORY_READ_SUCCESS", response));
	}

	// 문자 발송 템플릿 생성
	@PostMapping("/templates")
	public ResponseEntity<SuccessResponse<TemplateResDto>> createTemplate(@RequestBody @Valid CreateUpdateTemplateReqDto createUpdateTemplateReqDto) {
		Long agentId = SecurityUtil.getAuthenticatedAgent().getId();
		TemplateResDto response = templateService.createTemplate(createUpdateTemplateReqDto,agentId);
		return ResponseEntity.ok(SuccessResponse.success("문자 템플릿 생성에 성공했습니다.", "SMS_TEMPLATE_CREATE_SUCCESS",response));
	}

    // 문자 발송 템플릿 수정
	@PutMapping("/templates/{id}")
	public ResponseEntity<SuccessResponse<TemplateResDto>> updateTemplate(@RequestBody @Valid CreateUpdateTemplateReqDto createUpdateTemplateReqDto, @PathVariable Long id){
		Long agentId = SecurityUtil.getAuthenticatedAgent().getId();
		TemplateResDto response = templateService.updateTemplate(createUpdateTemplateReqDto,id,agentId);
		return ResponseEntity.ok(SuccessResponse.success("문자 템플릿 수정에 성공했습니다.", "SMS_TEMPLATE_UPDATE_SUCCESS",response));
	}

    // 문자 발송 템플릿 삭제
	@DeleteMapping("/templates/{id}")
	public ResponseEntity<SuccessResponse<Void>> deleteTemplate(@PathVariable Long id){
		Long agentId = SecurityUtil.getAuthenticatedAgent().getId();
		SmsTemplate response =  templateService.deleteTemplate(id,agentId);
		return ResponseEntity.ok(SuccessResponse.success("문자 템플릿" + response.getTitle() +" 삭제에 성공했습니다.", "SMS_TEMPLATE_DELETE_SUCCESS",null));
	}

    // 문자 발송 템플릿 상세 조회
	@GetMapping("/templates/{id}")
	public ResponseEntity<SuccessResponse<TemplateResDto>> findTemplate(@PathVariable Long id) {
		Long agentId = SecurityUtil.getAuthenticatedAgent().getId();
		TemplateResDto response = templateService.findTemplate(id,agentId);
		return ResponseEntity.ok(SuccessResponse.success("문자 템플릿" + response.getTitle() +"조회에 성공했습니다","SMS_TEMPLATE_FIND_SUCCESS",response));
	}

    // 문자 발송 템플릿 목록 조회
	// 같은 부동산에서 만든 템플릿을 전체 조회
	@GetMapping("/templates")
	public ResponseEntity<SuccessResponse<List<TemplateResDto>>> findAllTemplates(){
		Long agentId = SecurityUtil.getAuthenticatedAgent().getId();
		List<TemplateResDto> response = templateService.findAll(agentId);
		return ResponseEntity.ok(SuccessResponse.success("문자 템플릿 전체 조회에 성공했습니다.","SMS_TEMPLATE_FIND_ALL_SUCCESS",response));
	}
}