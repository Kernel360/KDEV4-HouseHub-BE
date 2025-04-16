package com.househub.backend.domain.sms.service;

import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.sms.dto.CreateUpdateTemplateReqDto;
import com.househub.backend.domain.sms.dto.SmsTemplateListResDto;
import com.househub.backend.domain.sms.dto.TemplateResDto;
import com.househub.backend.domain.sms.entity.SmsTemplate;

public interface TemplateService {

	// 문자 발송 템플릿 생성
	TemplateResDto createTemplate(CreateUpdateTemplateReqDto createUpdateTemplateReqDto, AgentResDto agentDto);

	// 문자 발송 템플릿 수정
	TemplateResDto updateTemplate(CreateUpdateTemplateReqDto createUpdateTemplateReqDto, Long id, AgentResDto agentDto);

	// 문자 발송 템플릿 삭제
	SmsTemplate deleteTemplate(Long id, AgentResDto agentDto);

	// 문자 발송 템플릿 목록 조회
	SmsTemplateListResDto findAll(String keyword, AgentResDto agentDto, Pageable adjustedPageable);

	// 문자 발송 템플릿 상세 조회
	TemplateResDto findTemplate(Long id, AgentResDto agentDto);
}