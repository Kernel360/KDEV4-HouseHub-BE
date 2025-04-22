package com.househub.backend.domain.inquiryTemplate.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.common.exception.NoAssociatedRealEstateException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.AgentStatus;
import com.househub.backend.domain.agent.entity.RealEstate;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.inquiryTemplate.dto.CreateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplateListResDto;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplatePreviewResDto;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplateResDto;
import com.househub.backend.domain.inquiryTemplate.dto.InquiryTemplateSharedResDto;
import com.househub.backend.domain.inquiryTemplate.dto.UpdateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplateSharedToken;
import com.househub.backend.domain.inquiryTemplate.entity.Question;
import com.househub.backend.domain.inquiryTemplate.exception.InactiveTemplateException;
import com.househub.backend.domain.inquiryTemplate.exception.InvalidShareTokenException;
import com.househub.backend.domain.inquiryTemplate.repository.InquiryTemplateRepository;
import com.househub.backend.domain.inquiryTemplate.repository.InquiryTemplateSharedTokenRepository;
import com.househub.backend.domain.inquiryTemplate.repository.QuestionRepository;
import com.househub.backend.domain.inquiryTemplate.service.InquiryTemplateCreateExecutor;
import com.househub.backend.domain.inquiryTemplate.service.InquiryTemplateReader;
import com.househub.backend.domain.inquiryTemplate.service.InquiryTemplateService;
import com.househub.backend.domain.inquiryTemplate.service.InquiryTemplateStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryTemplateServiceImpl implements InquiryTemplateService {
	private final InquiryTemplateReader inquiryTemplateReader;
	private final InquiryTemplateStore inquiryTemplateStore;
	private final InquiryTemplateCreateExecutor createExecutor;
	private final AgentRepository agentRepository;
	private final InquiryTemplateRepository inquiryTemplateRepository;
	private final QuestionRepository questionRepository;

	private final InquiryTemplateSharedTokenRepository sharedTokenRepository;

	/**
	 * 새로운 문의 템플릿을 생성합니다.
	 * @param agentId 문의 템플릿을 생성하는 중개사 ID
	 * @param reqDto 생성할 문의 템플릿의 정보를 담고 있는 요청 DTO
	 *               중개사 ID, 문의 템플릿 이름, 문의 템플릿 설명, 질문 목록, 활성화 여부
	 *               질문 목록은 질문 순서, 질문 내용, 답변 유형, 답변 선택지로 구성
	 */
	@Override
	@Transactional
	public void createNewInquiryTemplate(CreateInquiryTemplateReqDto reqDto, Long agentId) {
		createExecutor.execute(reqDto, agentId);
	}

	/**
	 * 문의 템플릿 목록을 조회합니다.
	 * @param active 활성화 여부 필터 (선택 사항)
	 * @param keyword 검색 키워드 (선택 사항)
	 * @param pageable 페이지네이션 정보
	 * @return 문의 템플릿 목록을 포함한 응답
	 */
	@Override
	public InquiryTemplateListResDto getInquiryTemplates(Boolean active, String keyword, Pageable pageable,
		Long agentId) {
		// 1. 중개사 ID로 중개사 존재 여부 확인
		Agent agent = findAgentById(agentId);

		// 2. 문의 템플릿 목록 조회
		Page<InquiryTemplateResDto> page = inquiryTemplateRepository.findAllByAgentIdAndFilters(agent.getId(),
				active,
				keyword,
				pageable)
			.map(template -> {
				// 가장 최근의 활성화된 공유 토큰 조회
				Optional<InquiryTemplateSharedToken> tokenOpt =
					sharedTokenRepository.findTopByTemplateAndActiveIsTrueOrderByCreatedAtDesc(template);

				String shareToken = tokenOpt.map(InquiryTemplateSharedToken::getShareToken).orElse(null);
				return InquiryTemplateResDto.fromEntity(template, shareToken);
			});

		return InquiryTemplateListResDto.fromPage(page);
	}

	/**
	 *
	 * @param keyword 검색 키워드
	 * @param pageable 페이지네이션 정보
	 * @return 문의 템플릿 목록을 포함한 응답
	 */
	@Transactional(readOnly = true)
	@Override
	public InquiryTemplateListResDto searchInquiryTemplates(String keyword, Pageable pageable, Long agentId) {
		// 1. 중개사 ID로 중개사 존재 여부 확인
		Agent agent = findAgentById(agentId);

		// 2. 문의 템플릿 목록 검색
		Page<InquiryTemplateResDto> page = inquiryTemplateRepository.findAllByAgentIdAndKeyword(agent.getId(),
				keyword,
				pageable)
			.map(template -> {
				// 가장 최근의 활성화된 공유 토큰 조회
				Optional<InquiryTemplateSharedToken> tokenOpt =
					sharedTokenRepository.findTopByTemplateAndActiveIsTrueOrderByCreatedAtDesc(template);

				String shareToken = tokenOpt.map(InquiryTemplateSharedToken::getShareToken).orElse(null);
				return InquiryTemplateResDto.fromEntity(template, shareToken);
			});

		return InquiryTemplateListResDto.fromPage(page);
	}

	/**
	 *
	 * @param templateId 문의 템플릿 ID
	 * @return 문의 템플릿 미리보기 응답
	 */
	@Override
	public InquiryTemplatePreviewResDto previewInquiryTemplate(Long templateId, Long agentId) {
		// 1. 중개사 ID로 중개사 존재 여부 확인
		Agent agent = findAgentById(agentId);

		// 2. 문의 템플릿 ID 와 중개사 ID로 문의 템플릿 조회
		InquiryTemplate inquiryTemplate = findInquiryTemplateByIdAndAgentId(templateId, agent.getId());

		// 3. 문의 템플릿에 속한 질문 목록 조회
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
	public void updateInquiryTemplate(Long templateId, UpdateInquiryTemplateReqDto reqDto, Long agentId) {
		// 1. 중개사 ID로 중개사 존재 여부 확인
		Agent agent = findAgentById(agentId);

		// 2. 문의 템플릿 ID로 문의 템플릿 조회
		InquiryTemplate inquiryTemplate = findInquiryTemplateByIdAndAgentId(templateId, agent.getId());

		// 3. 문의 템플릿 수정
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
		inquiryTemplateRepository.save(inquiryTemplate);
	}

	/**
	 *
	 * @param templateId 문의 템플릿 ID
	 */
	@Override
	public void deleteInquiryTemplate(Long templateId, Long agentId) {
		// 1. 중개사 ID로 중개사 존재 여부 확인
		Agent agent = findAgentById(agentId);

		// 2. 문의 템플릿 ID로 문의 템플릿 조회
		InquiryTemplate inquiryTemplate = findInquiryTemplateByIdAndAgentId(templateId, agent.getId());

		// 3. 문의 템플릿 삭제
		inquiryTemplateRepository.delete(inquiryTemplate);
	}

	@Override
	public InquiryTemplateSharedResDto getInquiryTemplateByShareToken(String shareToken) {
		InquiryTemplateSharedToken shareTokenEntity = sharedTokenRepository.findByShareTokenAndActiveTrue(shareToken)
			.orElseThrow(() -> new InvalidShareTokenException("유효하지 않거나 존재하지 않는 공유 토큰입니다."));

		InquiryTemplate inquiryTemplate = shareTokenEntity.getTemplate();
		if (!Boolean.TRUE.equals(inquiryTemplate.getActive())) {
			throw new InactiveTemplateException("비활성화된 템플릿입니다. 접근이 제한됩니다.");
		}

		// 질문 리스트 가져오기 (질문 순서 기준 정렬)
		List<Question> questions = inquiryTemplate.getQuestions().stream()
			.sorted(Comparator.comparingInt(Question::getQuestionOrder))
			.toList();

		return InquiryTemplateSharedResDto.fromEntity(inquiryTemplate, questions);

	}

	/**
	 * 중개사 ID로 중개사 조회 후, 해당 중개사가 소속된 부동산을 반환합니다.
	 * @param agentId 중개사 ID
	 * @return 부동산 객체
	 * @throws NoAssociatedRealEstateException 중개사에 소속된 부동산이 없는 경우
	 */
	private RealEstate getRealEstateByAgentId(Long agentId) {
		// 중개사 ID로 중개사 조회하는데, status 가 반드시 ACTIVE 인 중개사만 조회
		Agent agent = findAgentById(agentId);

		// 중개사 소속 부동산이 없으면 예외 처리
		if (agent.getRealEstate() == null) {
			throw new NoAssociatedRealEstateException();
		}

		return agent.getRealEstate();
	}

	/**
	 * 템플릿 ID 및 중개사 ID 로 문의 템플릿을 조회합니다.
	 * @param templateId 문의 템플릿 ID
	 * @param agentId 중개사 ID
	 * @return 문의 템플릿 엔티티
	 * @throws ResourceNotFoundException 해당 문의 템플릿을 찾을 수 없는 경우
	 */
	private InquiryTemplate findInquiryTemplateByIdAndAgentId(Long templateId, Long agentId) {
		return inquiryTemplateRepository.findByIdAndAgentId(templateId, agentId)
			.orElseThrow(() -> new ResourceNotFoundException("해당 문의 템플릿을 찾을 수 없습니다.", "INQUIRY_TEMPLATE_NOT_FOUND"));
	}

	/**
	 *
	 * @param agentId 중개사 ID
	 * @return 중개사 엔티티
	 * @throws ResourceNotFoundException 해당 중개사를 찾을 수 없는 경우
	 */
	private Agent findAgentById(Long agentId) {
		// agentId 로 중개사 조회하는데, status 가 반드시 ACTIVE 인 중개사만 조회
		return agentRepository.findByIdAndStatus(agentId, AgentStatus.ACTIVE)
			.orElseThrow(() -> new ResourceNotFoundException("해당 중개사를 찾을 수 없습니다.", "AGENT_NOT_FOUND"));
	}
}
