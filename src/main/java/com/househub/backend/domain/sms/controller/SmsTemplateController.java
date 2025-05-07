package com.househub.backend.domain.sms.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.sms.dto.CreateUpdateTemplateReqDto;
import com.househub.backend.domain.sms.dto.SmsTemplateListResDto;
import com.househub.backend.domain.sms.dto.TemplateResDto;
import com.househub.backend.domain.sms.entity.SmsTemplate;
import com.househub.backend.domain.sms.service.SmsTemplateService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sms-templates")
public class SmsTemplateController {

	private final SmsTemplateService smsTemplateService;

	@Operation(
		summary = "문자 발송 템플릿 생성",
		description = "문자 발송에 사용할 템플릿을 생성합니다."
	)
	@PostMapping("")
	public ResponseEntity<SuccessResponse<TemplateResDto>> createTemplate(@RequestBody @Valid CreateUpdateTemplateReqDto createUpdateTemplateReqDto) {
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		TemplateResDto response = smsTemplateService.create(createUpdateTemplateReqDto,agentDto);
		return ResponseEntity.ok(SuccessResponse.success("문자 템플릿 생성에 성공했습니다.", "SMS_TEMPLATE_CREATE_SUCCESS",response));
	}

	@Operation(
		summary = "문자 발송 템플릿 수정",
		description = "기존 문자 발송 템플릿의 내용을 수정합니다."
	)
	@PutMapping("/{id}")
	public ResponseEntity<SuccessResponse<TemplateResDto>> updateTemplate(@RequestBody @Valid CreateUpdateTemplateReqDto createUpdateTemplateReqDto, @PathVariable Long id){
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		TemplateResDto response = smsTemplateService.update(createUpdateTemplateReqDto,id,agentDto);
		return ResponseEntity.ok(SuccessResponse.success("문자 템플릿 수정에 성공했습니다.", "SMS_TEMPLATE_UPDATE_SUCCESS",response));
	}

	@Operation(
		summary = "문자 발송 템플릿 삭제",
		description = "특정 문자 발송 템플릿을 삭제합니다."
	)
	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponse<Void>> deleteTemplate(@PathVariable Long id){
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		SmsTemplate response =  smsTemplateService.delete(id,agentDto);
		return ResponseEntity.ok(SuccessResponse.success("문자 템플릿" + response.getTitle() +" 삭제에 성공했습니다.", "SMS_TEMPLATE_DELETE_SUCCESS",null));
	}

	@Operation(
		summary = "문자 발송 템플릿 상세 조회",
		description = "특정 문자 발송 템플릿의 상세 정보를 조회합니다."
	)
	@GetMapping("/{id}")
	public ResponseEntity<SuccessResponse<TemplateResDto>> findTemplate(@PathVariable Long id) {
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		TemplateResDto response = smsTemplateService.findById(id,agentDto);
		return ResponseEntity.ok(SuccessResponse.success("문자 템플릿" + response.getTitle() +"조회에 성공했습니다","SMS_TEMPLATE_FIND_SUCCESS",response));
	}

	@Operation(
		summary = "문자 발송 템플릿 목록 조회",
		description = "같은 부동산에서 만든 문자 발송 템플릿 전체를 키워드와 페이지네이션으로 조회합니다."
	)
	@GetMapping("")
	public ResponseEntity<SuccessResponse<SmsTemplateListResDto>> findAllTemplates(
		@RequestParam(required = false) String keyword,
		Pageable pageable
	){
		int page = Math.max(pageable.getPageNumber() -1, 0);
		int size = pageable.getPageSize();

		Pageable adjustedPageable = PageRequest.of(page,size, pageable.getSort());

		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		SmsTemplateListResDto response = smsTemplateService.findAll(keyword, agentDto, adjustedPageable);
		return ResponseEntity.ok(SuccessResponse.success("문자 템플릿 전체 조회에 성공했습니다.","SMS_TEMPLATE_FIND_ALL_SUCCESS", response));
	}
}
