package com.househub.backend.domain.inquiryTemplate.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.common.exception.AlreadyExistsException;
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
import com.househub.backend.domain.inquiryTemplate.dto.UpdateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.Question;
import com.househub.backend.domain.inquiryTemplate.repository.InquiryTemplateRepository;
import com.househub.backend.domain.inquiryTemplate.repository.QuestionRepository;
import com.househub.backend.domain.inquiryTemplate.service.InquiryTemplateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryTemplateServiceImpl implements InquiryTemplateService {
	private final AgentRepository agentRepository;
	private final InquiryTemplateRepository inquiryTemplateRepository;
	private final QuestionRepository questionRepository;

	/**
	 * 새로운 문의 템플릿을 생성합니다.
	 * @param agentId 문의 템플릿을 생성하는 중개사 ID
	 * @param reqDto 생성할 문의 템플릿의 정보를 담고 있는 요청 DTO
	 *               중개사 ID, 문의 템플릿 이름, 문의 템플릿 설명, 질문 목록, 활성화 여부
	 *               질문 목록은 질문 순서, 질문 내용, 답변 유형, 답변 선택지로 구성
	 */
	@Override
	@Transactional
	public void createNewInquiryTemplate(CreateInquiryTemplateReqDto reqDto, Long agentId) throws
		AlreadyExistsException {
		// 1. 중개사 ID로 부동산 존재 여부 확인
		RealEstate realEstate = getRealEstateByAgentId(agentId);

		// 2. 동일한 이름의 문의 템플릿이 이미 존재하는지 확인
		boolean exists = inquiryTemplateRepository.existsByRealEstateIdAndName(realEstate.getId(),
			reqDto.getName());
		if (exists) {
			throw new AlreadyExistsException("이미 동일한 이름의 문의 템플릿이 존재합니다.", "DUPLICATED_INQUIRY_TEMPLATE_NAME");
		}

		// 3. 문의 템플릿 생성
		InquiryTemplate inquiryTemplate = InquiryTemplate.fromDto(reqDto, realEstate);
		inquiryTemplateRepository.save(inquiryTemplate);

		// 3. 질문 목록 생성 및 저장
		List<Question> questions = reqDto.getQuestions().stream()
			.map(questionDto -> Question.fromDto(questionDto, inquiryTemplate))
			.collect(Collectors.toList());
		questionRepository.saveAll(questions);
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
		// active 가 null 나 keyword 가 null 인 걸 고려해야 해.
		// 1. 중개사 ID로 부동산 존재 여부 확인
		RealEstate realEstate = getRealEstateByAgentId(agentId);

		// 2. 문의 템플릿 목록 조회
		Page<InquiryTemplateResDto> page = inquiryTemplateRepository.findAllByRealEstateIdAndFilters(realEstate.getId(),
				active,
				keyword,
				pageable)
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
	public InquiryTemplateListResDto searchInquiryTemplates(String keyword, Pageable pageable, Long agentId) {
		// 1. 중개사 ID로 부동산 존재 여부 확인
		RealEstate realEstate = getRealEstateByAgentId(agentId);

		// 2. 문의 템플릿 목록 검색
		Page<InquiryTemplateResDto> page = inquiryTemplateRepository.findAllByRealEstateIdAndKeyword(realEstate.getId(),
				keyword,
				pageable)
			.map(InquiryTemplateResDto::fromEntity);

		return InquiryTemplateListResDto.fromPage(page);
	}

	/**
	 *
	 * @param templateId 문의 템플릿 ID
	 * @return 문의 템플릿 미리보기 응답
	 */
	@Override
	public InquiryTemplatePreviewResDto previewInquiryTemplate(Long templateId, Long agentId) {
		// 1. 중개사 ID로 부동산 존재 여부 확인
		RealEstate realEstate = getRealEstateByAgentId(agentId);

		// 2. 문의 템플릿 ID 와 중개사 ID로 문의 템플릿 조회
		InquiryTemplate inquiryTemplate = findInquiryTemplateByIdAndRealEstateId(templateId, realEstate.getId());

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
		// 1. 중개사 ID로 부동산 존재 여부 확인
		RealEstate realEstate = getRealEstateByAgentId(agentId);

		// 2. 문의 템플릿 ID로 문의 템플릿 조회
		InquiryTemplate inquiryTemplate = findInquiryTemplateByIdAndRealEstateId(templateId, realEstate.getId());

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
		// 1. 중개사 ID로 부동산 존재 여부 확인
		RealEstate realEstate = getRealEstateByAgentId(agentId);

		// 2. 문의 템플릿 ID로 문의 템플릿 조회
		InquiryTemplate inquiryTemplate = findInquiryTemplateByIdAndRealEstateId(templateId, realEstate.getId());

		// 3. 문의 템플릿 삭제
		inquiryTemplateRepository.delete(inquiryTemplate);
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
	 * 템플릿 ID 및 부동산 객체 로 문의 템플릿을 조회합니다.
	 * @param templateId 문의 템플릿 ID
	 * @param realEstateId 부동산 ID
	 * @return 문의 템플릿 엔티티
	 * @throws ResourceNotFoundException 해당 문의 템플릿을 찾을 수 없는 경우
	 */
	private InquiryTemplate findInquiryTemplateByIdAndRealEstateId(Long templateId, Long realEstateId) {
		return inquiryTemplateRepository.findByIdAndRealEstateId(templateId, realEstateId)
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
