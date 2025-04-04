package com.househub.backend.domain.sms.service;

import java.util.List;

import com.househub.backend.domain.sms.dto.CreateUpdateTemplateReqDto;
import com.househub.backend.domain.sms.dto.TemplateResDto;
import com.househub.backend.domain.sms.entity.SmsTemplate;

public interface TemplateService {

	// 문자 발송 템플릿 생성
	TemplateResDto createTemplate(CreateUpdateTemplateReqDto createUpdateTemplateReqDto, Long agentId);

	// 문자 발송 템플릿 수정
	TemplateResDto updateTemplate(CreateUpdateTemplateReqDto createUpdateTemplateReqDto, Long id, Long agentId);

	// 문자 발송 템플릿 삭제
	SmsTemplate deleteTemplate(Long id, Long agentId);

	// 문자 발송 템플릿 목록 조회
	List<TemplateResDto> findAll(Long agentId);

	// 문자 발송 템플릿 상세 조회
	TemplateResDto findTemplate(Long id, Long agentId);
}