package com.househub.backend.domain.inquiryForm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.inquiryForm.dto.CreateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryForm.dto.InquiryTemplateListResDto;
import com.househub.backend.domain.inquiryForm.dto.InquiryTemplatePreviewResDto;
import com.househub.backend.domain.inquiryForm.dto.InquiryTemplateResDto;
import com.househub.backend.domain.inquiryForm.dto.UpdateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryForm.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryForm.entity.Question;
import com.househub.backend.domain.inquiryForm.repository.InquiryTemplateRepository;
import com.househub.backend.domain.inquiryForm.repository.QuestionRepository;
import com.househub.backend.domain.inquiryForm.service.InquiryTemplateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryTemplateServiceImpl implements InquiryTemplateService {
	private final InquiryTemplateRepository inquiryTemplateRepository;
	private final QuestionRepository questionRepository;

	/**
	 *
	 * @param reqDto 생성할 문의 템플릿의 정보를 담고 있는 요청 DTO
	 */
	@Override
	@Transactional
	public void createNewInquiryTemplate(CreateInquiryTemplateReqDto reqDto) {
		// 1. 문의 템플릿 생성
		InquiryTemplate inquiryTemplate = InquiryTemplate.fromDto(reqDto);
		inquiryTemplateRepository.save(inquiryTemplate);

		// 2. 질문 목록 생성 및 저장
		List<Question> questions = reqDto.getQuestions().stream()
			.map(questionDto -> Question.fromDto(questionDto, inquiryTemplate))
			.collect(Collectors.toList());
		questionRepository.saveAll(questions);
	}

	/**
	 *
	 * @param isActive 활성화 여부 필터 (선택 사항)
	 * @param pageable 페이지네이션 정보
	 * @return 문의 템플릿 목록을 포함한 응답
	 */
	@Override
	public InquiryTemplateListResDto getInquiryTemplates(Boolean isActive, Pageable pageable) {
		Page<InquiryTemplateResDto> page = inquiryTemplateRepository.findAllByFilters(isActive, pageable)
			.map(InquiryTemplateResDto::fromEntity);

		return InquiryTemplateListResDto.fromPage(page);
	}

	/**
	 *
	 * @param keyword 검색 키워드
	 * @param pageable 페이지네이션 정보
	 * @return 문의 템플릿 목록을 포함한 응답
	 */
	@Override
	public InquiryTemplateListResDto searchInquiryTemplates(String keyword, Pageable pageable) {
		Page<InquiryTemplateResDto> page = inquiryTemplateRepository.findAllByKeyword(keyword, pageable)
			.map(InquiryTemplateResDto::fromEntity);

		return InquiryTemplateListResDto.fromPage(page);
	}

	/**
	 *
	 * @param templateId 문의 템플릿 ID
	 * @return 문의 템플릿 미리보기 응답
	 */
	@Override
	public InquiryTemplatePreviewResDto previewInquiryTemplate(Long templateId) {
		InquiryTemplate inquiryTemplate = findInquiryTemplateById(templateId);

		List<Question> questions = questionRepository.findAllByInquiryTemplate(inquiryTemplate);

		return InquiryTemplatePreviewResDto.fromEntity(inquiryTemplate, questions);
	}

	/**
	 *
	 * @param templateId 문의 템플릿 ID
	 * @param reqDto 수정할 문의 템플릿의 정보를 담고 있는 요청 DTO
	 */
	@Transactional
	@Override
	public void updateInquiryTemplate(Long templateId, UpdateInquiryTemplateReqDto reqDto) {
		InquiryTemplate inquiryTemplate = findInquiryTemplateById(templateId);

		if (reqDto.getQuestions() != null) {
			reqDto.getQuestions().forEach(questionDto -> {
				Question question = inquiryTemplate.getQuestions().stream()
					.filter(q -> q.getQuestionOrder() == questionDto.getQuestionOrder())
					.findFirst()
					.orElseThrow(() -> new ResourceNotFoundException("해당 질문을 찾을 수 없습니다.", "QUESTION_NOT_FOUND"));

				question.update(questionDto);
			});
		}

		inquiryTemplate.update(reqDto);
		inquiryTemplateRepository.save(inquiryTemplate);
	}

	/**
	 *
	 * @param templateId 문의 템플릿 ID
	 */
	@Override
	public void deleteInquiryTemplate(Long templateId) {
		InquiryTemplate inquiryTemplate = findInquiryTemplateById(templateId);
		inquiryTemplateRepository.delete(inquiryTemplate);
	}

	/**
	 *
	 * @param templateId 문의 템플릿 ID
	 * @return 문의 템플릿 엔티티
	 * @throws ResourceNotFoundException 해당 문의 템플릿을 찾을 수 없는 경우
	 */
	public InquiryTemplate findInquiryTemplateById(Long templateId) {
		return inquiryTemplateRepository.findById(templateId)
			.orElseThrow(() -> new ResourceNotFoundException("해당 문의 템플릿을 찾을 수 없습니다.", "INQUIRY_TEMPLATE_NOT_FOUND"));
	}
}
