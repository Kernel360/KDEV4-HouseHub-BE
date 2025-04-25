package com.househub.backend.domain.inquiryTemplate.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.inquiryTemplate.dto.CreateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplateListResDto;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplatePreviewResDto;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplateResDto;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplateSearchCommand;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplateSharedResDto;
import com.househub.backend.domain.inquiryTemplate.dto.UpdateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplateSharedToken;
import com.househub.backend.domain.inquiryTemplate.entity.Question;
import com.househub.backend.domain.inquiryTemplate.service.InquiryTemplateReader;
import com.househub.backend.domain.inquiryTemplate.service.InquiryTemplateService;
import com.househub.backend.domain.inquiryTemplate.service.InquiryTemplateStore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryTemplateServiceImpl implements InquiryTemplateService {
	private final InquiryTemplateReader inquiryTemplateReader;
	private final InquiryTemplateStore inquiryTemplateStore;

	/**
	 * 새로운 문의 템플릿을 생성합니다.
	 * @param agent 문의 템플릿을 생성하는 중개사
	 * @param reqDto 생성할 문의 템플릿의 정보를 담고 있는 요청 DTO
	 *               중개사 ID, 문의 템플릿 이름, 문의 템플릿 설명, 질문 목록, 활성화 여부
	 *               질문 목록은 질문 순서, 질문 내용, 답변 유형, 답변 선택지로 구성
	 */
	@Override
	@Transactional
	public void createNewInquiryTemplate(CreateInquiryTemplateReqDto reqDto, AgentResDto agent) {
		// 동일한 이름의 문의 템플릿이 이미 존재하는지 확인
		boolean exists = inquiryTemplateReader.existsByAgentIdAndName(agent.getId(),
			reqDto.getName());
		if (exists) {
			throw new AlreadyExistsException("이미 동일한 이름의 문의 템플릿이 존재합니다.", "DUPLICATED_INQUIRY_TEMPLATE_NAME");
		}

		// 문의 템플릿 생성
		InquiryTemplate inquiryTemplate = InquiryTemplate.fromDto(reqDto, agent.toEntity());
		inquiryTemplateStore.createTemplate(inquiryTemplate);

		// 질문 목록 생성 및 저장
		List<Question> questions = reqDto.getQuestions().stream()
			.map(questionDto -> Question.fromDto(questionDto, inquiryTemplate))
			.collect(Collectors.toList());
		inquiryTemplateStore.createQuestions(questions);

		// 공유 토큰 저장 (InquiryTemplateSharedToken)
		// 문의 템플릿 생성 시 공유 토큰도 무조건 함께 생성하되, 활성화 여부는 템플릿의 isActive 상태를 반영한다.
		// 공유 토큰은 고객이 해당 템플릿에 접근할 수 있는 식별자이며, 관리자가 공유 여부를 토글하여 사용 여부를 제어할 수 있다.
		InquiryTemplateSharedToken token = InquiryTemplateSharedToken.create(inquiryTemplate);
		token.setActive(inquiryTemplate.getActive());
		inquiryTemplateStore.createToken(token);
	}

	/**
	 * 문의 템플릿 목록을 조회합니다.
	 * @param active 활성화 여부 필터 (선택 사항)
	 * @param keyword 검색 키워드 (선택 사항)
	 * @param type 템플릿 타입 (선택 사항)
	 * @param pageable 페이지네이션 정보
	 * @param agent 문의 템플릿을 조회하는 중개사
	 *
	 * @return 문의 템플릿 목록을 포함한 응답
	 */
	@Transactional(readOnly = true)
	@Override
	public InquiryTemplateListResDto getInquiryTemplates(Boolean active, String keyword, String type,
		Pageable pageable,
		AgentResDto agent) {
		// 문의 템플릿 목록 조회
		InquiryTemplateSearchCommand command = new InquiryTemplateSearchCommand(
			agent.getId(),
			active,
			keyword,
			type,
			pageable
		);
		Page<InquiryTemplateResDto> page = inquiryTemplateReader.searchInquiryTemplates(command);

		return InquiryTemplateListResDto.fromPage(page);
	}

	/**
	 *
	 * @param templateId 문의 템플릿 ID
	 * @param agent 문의 템플릿을 조회하는 중개사
	 * @return 문의 템플릿 미리보기 응답
	 */
	@Transactional(readOnly = true)
	@Override
	public InquiryTemplatePreviewResDto previewInquiryTemplate(Long templateId, AgentResDto agent) {
		// 문의 템플릿 ID 와 중개사 ID로 문의 템플릿 및 그와 연결된 질문 목록 조회
		InquiryTemplate inquiryTemplate = inquiryTemplateReader.findInquiryTemplateWithQuestionsByIdAndAgentId(
			templateId,
			agent.getId());
		return InquiryTemplatePreviewResDto.fromEntity(inquiryTemplate);
	}

	/**
	 *
	 * @param templateId 문의 템플릿 ID
	 * @param reqDto 수정할 문의 템플릿의 정보를 담고 있는 요청 DTO
	 */
	@Transactional
	@Override
	public void updateInquiryTemplate(Long templateId, UpdateInquiryTemplateReqDto reqDto, AgentResDto agent) {
		// 1. 문의 템플릿 ID로 문의 템플릿 조회
		InquiryTemplate inquiryTemplate = inquiryTemplateReader.findInquiryTemplateByIdAndAgentId(templateId,
			agent.getId());

		// 2. 문의 템플릿 수정
		if (reqDto.getQuestions() != null) {
			// 기존 질문들을 순서(questionOrder)를 키로 하는 맵으로 변환
			Map<Integer, Question> existingQuestionsMap = inquiryTemplate.getQuestions().stream()
				.collect(Collectors.toMap(Question::getQuestionOrder, q -> q));

			List<Question> updatedQuestions = reqDto.getQuestions().stream()
				.map(questionDto -> {
					int questionOrder = questionDto.getQuestionOrder();
					Question existingQuestion = existingQuestionsMap.get(questionOrder);

					if (existingQuestion != null) {
						// 기존 질문 업데이트
						existingQuestion.update(questionDto);
						return existingQuestion;
					} else {
						// 새로 추가된 질문 생성 및 연결
						return Question.fromDto(questionDto, inquiryTemplate);
					}
				})
				.toList();

			// 기존 질문 목록을 업데이트된 질문 목록으로 교체
			inquiryTemplate.getQuestions().clear();
			inquiryTemplate.getQuestions().addAll(updatedQuestions);
		}

		inquiryTemplate.update(reqDto);
		inquiryTemplateStore.updateTemplate(inquiryTemplate);
	}

	/**
	 *
	 * @param templateId 문의 템플릿 ID
	 */
	@Transactional
	@Override
	public void deleteInquiryTemplate(Long templateId, AgentResDto agent) {
		// 1. 문의 템플릿 ID로 문의 템플릿 조회
		InquiryTemplate inquiryTemplate = inquiryTemplateReader.findInquiryTemplateByIdAndAgentId(templateId,
			agent.getId());

		// 2. 문의 템플릿 삭제
		inquiryTemplate.softDelete();
		inquiryTemplateStore.deleteTemplate(inquiryTemplate);
	}

	@Transactional(readOnly = true)
	@Override
	public InquiryTemplateSharedResDto getInquiryTemplateByShareToken(String shareToken) {
		InquiryTemplate inquiryTemplate = inquiryTemplateReader.findInquiryTemplateWithQuestionsByActiveShareToken(
			shareToken);

		// 질문 리스트 가져오기 (질문 순서 기준 정렬)
		List<Question> questions = inquiryTemplate.getQuestions().stream()
			.sorted(Comparator.comparingInt(Question::getQuestionOrder))
			.toList();

		return InquiryTemplateSharedResDto.fromEntity(inquiryTemplate, questions);
	}
}
