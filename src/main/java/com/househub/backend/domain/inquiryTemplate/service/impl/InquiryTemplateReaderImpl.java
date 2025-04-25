package com.househub.backend.domain.inquiryTemplate.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplateResDto;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplateSearchCommand;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplateSharedToken;
import com.househub.backend.domain.inquiryTemplate.enums.InquiryType;
import com.househub.backend.domain.inquiryTemplate.exception.InactiveTemplateException;
import com.househub.backend.domain.inquiryTemplate.repository.InquiryTemplateRepository;
import com.househub.backend.domain.inquiryTemplate.repository.InquiryTemplateSharedTokenRepository;
import com.househub.backend.domain.inquiryTemplate.service.InquiryTemplateReader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class InquiryTemplateReaderImpl implements InquiryTemplateReader {
	private final InquiryTemplateRepository inquiryTemplateRepository;
	private final InquiryTemplateSharedTokenRepository sharedTokenRepository;

	@Override
	public InquiryTemplate findByToken(String token) {
		InquiryTemplateSharedToken shareToken = sharedTokenRepository.findByShareTokenAndActiveTrue(token)
			.orElseThrow(() -> new BusinessException(ErrorCode.INVALID_SHARED_TOKEN));

		return shareToken.getTemplate();
	}

	@Override
	public boolean existsByAgentIdAndName(Long agentId, String name) {
		return inquiryTemplateRepository.existsByAgentIdAndName(agentId,
			name);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<InquiryTemplateResDto> searchInquiryTemplates(InquiryTemplateSearchCommand command) {
		Long agentId = command.agentId();
		Boolean active = command.active();
		String keyword = command.keyword();
		String type = command.type();
		Pageable pageable = command.pageable();

		log.info("문의 템플릿 조회 (ReaderImpl) - command={}", command);

		Page<InquiryTemplateResDto> page = inquiryTemplateRepository.findAllByAgentIdAndFilters(
				agentId,
				active,
				keyword,
				type != null ? InquiryType.fromKorean(type) : null,
				pageable
			)
			.map(this::convertToInquiryTemplateResDtoWithShareToken);

		return page;
	}

	/**
	 * 템플릿 ID 및 중개사 ID 로 문의 템플릿을 조회합니다.
	 * @param templateId 문의 템플릿 ID
	 * @param agentId 중개사 ID
	 * @return 문의 템플릿 엔티티
	 * @throws ResourceNotFoundException 해당 문의 템플릿을 찾을 수 없는 경우
	 */
	@Override
	public InquiryTemplate findInquiryTemplateByIdAndAgentId(Long templateId, Long agentId) {
		return inquiryTemplateRepository.findByIdAndAgentId(templateId, agentId)
			.orElseThrow(() -> new ResourceNotFoundException("해당 문의 템플릿을 찾을 수 없습니다.", "INQUIRY_TEMPLATE_NOT_FOUND"));
	}

	@Override
	public InquiryTemplate findInquiryTemplateWithQuestionsByIdAndAgentId(Long templateId, Long agentId) {
		return inquiryTemplateRepository.findWithQuestionsByIdAndAgentId(templateId, agentId)
			.orElseThrow(() -> new ResourceNotFoundException("해당 문의 템플릿을 찾을 수 없습니다.", "INQUIRY_TEMPLATE_NOT_FOUND"));
	}

	@Override
	public InquiryTemplate findInquiryTemplateWithQuestionsByActiveShareToken(String shareToken) {
		InquiryTemplateSharedToken token = sharedTokenRepository.findByShareTokenAndActiveTrue(shareToken)
			.orElseThrow(() -> new BusinessException(ErrorCode.INVALID_SHARED_TOKEN));

		return inquiryTemplateRepository.findActiveTemplateWithQuestionsById(token.getTemplate().getId())
			.orElseThrow(() -> new InactiveTemplateException("비활성화된 템플릿입니다. 접근이 제한됩니다."));
	}

	private InquiryTemplateResDto convertToInquiryTemplateResDtoWithShareToken(InquiryTemplate template) {
		// 가장 최근의 활성화된 공유 토큰 조회
		Optional<InquiryTemplateSharedToken> tokenOpt =
			sharedTokenRepository.findTopByTemplateAndActiveIsTrueOrderByCreatedAtDesc(template);

		String shareToken = tokenOpt.map(InquiryTemplateSharedToken::getShareToken).orElse(null);
		return InquiryTemplateResDto.fromEntity(template, shareToken);
	}
}
