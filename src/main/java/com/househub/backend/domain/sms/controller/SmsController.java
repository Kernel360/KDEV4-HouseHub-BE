package com.househub.backend.domain.sms.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.common.util.SecurityUtil;
import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.sms.dto.AligoHistoryReqDto;
import com.househub.backend.domain.sms.dto.AligoHistoryResDto;
import com.househub.backend.domain.sms.dto.CreateUpdateTemplateReqDto;
import com.househub.backend.domain.sms.dto.SendSmsReqDto;
import com.househub.backend.domain.sms.dto.SendSmsResDto;
import com.househub.backend.domain.sms.dto.SmsListResDto;
import com.househub.backend.domain.sms.dto.SmsTemplateListResDto;
import com.househub.backend.domain.sms.dto.TemplateResDto;
import com.househub.backend.domain.sms.entity.SmsTemplate;
import com.househub.backend.domain.sms.service.SmsService;
import com.househub.backend.domain.sms.service.SmsTemplateService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sms")
public class SmsController {
	private final SmsService smsService;

	@Operation(
		summary = "문자 전송",
		description = "고객에게 문자를 전송합니다."
	)
	@PostMapping("/send")
	public ResponseEntity<SuccessResponse<SendSmsResDto>> sendSms(@RequestBody @Valid SendSmsReqDto sendSmsReqDto) {
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		SendSmsResDto response = smsService.sendSms(sendSmsReqDto, agentDto);
		return ResponseEntity.ok(SuccessResponse.success("문자 전송에 성공했습니다.", "SMS_SEND_SUCCESS", response));
	}

	@Operation(
		summary = "문자 단건 조회",
		description = "특정 문자 발송 내역을 상세 조회합니다."
	)
	@GetMapping("/{id}")
	public ResponseEntity<SuccessResponse<SendSmsResDto>> getSms(@PathVariable Long id) {
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		SendSmsResDto response = smsService.findById(id, agentDto);

		return ResponseEntity.ok(SuccessResponse.success("문자 단건 조회에 성공했습니다.","SMS_READ_SUCCESS",response));
	}

	@Operation(
		summary = "문자 전체 조회",
		description = "모든 문자 발송 내역을 키워드와 페이지네이션으로 조회합니다."
	)
	@GetMapping("")
	public ResponseEntity<SuccessResponse<SmsListResDto>> getAll(
		@RequestParam(required = false) String keyword,
		Pageable pageable,
		@RequestParam(required = false) Long templateId
	){
		int page = Math.max(pageable.getPageNumber()-1,0);
		int size = pageable.getPageSize();

		Pageable adjustedPageable = PageRequest.of(page,size, pageable.getSort());

		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		SmsListResDto response = smsService.findAllByKeyword(keyword, agentDto, adjustedPageable,templateId);
		return ResponseEntity.ok(SuccessResponse.success("문자 전체 조회에 성공했습니다.", "ALL_SMS_READ_SUCCESS",response));
	}

	@Operation(
		summary = "문자 사용 금액 조회",
		description = "중개사의 한달간의 문자 서비스 사용 금액을 조회합니다."
	)
	@GetMapping("/cost")
	public ResponseEntity<SuccessResponse<Float>> getCost(){
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		float response = smsService.findSmsCostByAgentId(agentDto.getId());
		return ResponseEntity.ok(SuccessResponse.success("문자 사용 금액 조회에 성공했습니다.","SMS_COST_READ_SUCCESS",response));
	}
}