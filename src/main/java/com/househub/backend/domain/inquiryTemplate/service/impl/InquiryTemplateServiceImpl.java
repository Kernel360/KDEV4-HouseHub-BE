package com.househub.backend.domain.inquiryTemplate.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
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
		// 1. 기존 문의 템플릿 조회
		InquiryTemplate inquiryTemplate = inquiryTemplateReader.findInquiryTemplateByIdAndAgentId(templateId,
			agent.getId());

		// 문의 템플릿 제목 수정 불가
		if (!inquiryTemplate.getName().equals(reqDto.getName())) {
			throw new BusinessException(ErrorCode.INVALID_INQUIRY_TEMPLATE_TITLE_MODIFICATION);
		}

		// 템플릿 유형 수정 불가
		if (!inquiryTemplate.getType().equals(reqDto.getInquiryType())) {
			throw new BusinessException(ErrorCode.INVALID_INQUIRY_TEMPLATE_TYPE_MODIFICATION);
		}

		// 2. 새로운 버전 생성: 기존 템플릿 복제 후 새로운 버전으로 저장
		String newVersion = generateNextVersion(inquiryTemplate.getVersion());
		log.info("New version generated: {}", newVersion);

		InquiryTemplate newInquiryTemplate = InquiryTemplate.builder()
			.name(inquiryTemplate.getName())
			.type(inquiryTemplate.getType())
			.version(newVersion) // 새로운 버전 설정
			.questions(new ArrayList<>()) // 새로운 질문 목록 초기화
			.agent(inquiryTemplate.getAgent()) // 기존 에이전트 정보 그대로
			.active(true) // 새로운 템플릿 활성화
			.build();

		// 3. 기존 질문 목록을 업데이트된 질문 목록으로 교체
		if (reqDto.getQuestions() != null) {
			List<Question> updatedQuestions = reqDto.getQuestions().stream()
				.map(questionDto -> Question.fromDto(questionDto, newInquiryTemplate))
				.toList();

			// 새로운 문의 템플릿에 질문 추가
			newInquiryTemplate.getQuestions().addAll(updatedQuestions);
		}

		// 4. 새로운 템플릿 저장
		inquiryTemplateStore.createTemplate(newInquiryTemplate);

		InquiryTemplateSharedToken token = InquiryTemplateSharedToken.create(newInquiryTemplate);
		token.setActive(newInquiryTemplate.getActive());
		inquiryTemplateStore.createToken(token);

		// 5. 기존 템플릿 비활성화 처리
		inquiryTemplate.inactivate();
		inquiryTemplateStore.updateTemplate(inquiryTemplate);

		// 기존 템플릿과 연결된 활성화된 토큰들을 조회하여 비활성화 처리
		List<InquiryTemplateSharedToken> existingTokens =
			inquiryTemplateReader.findAllActiveTokensByTemplateId(inquiryTemplate.getId());
		existingTokens.forEach(existingToken -> {
			log.info("{}", existingToken.getShareToken());
			existingToken.inactivate();
			inquiryTemplateStore.updateToken(existingToken);
		});
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

		// 3. 기존 템플릿과 연결된 활성화된 토큰들을 조회하여 비활성화 처리
		List<InquiryTemplateSharedToken> existingTokens =
			inquiryTemplateReader.findAllActiveTokensByTemplateId(inquiryTemplate.getId());
		existingTokens.forEach(existingToken -> {
			log.info("{}", existingToken.getShareToken());
			existingToken.inactivate();
			inquiryTemplateStore.updateToken(existingToken);
		});
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

	// 새로운 버전 생성 로직 (주 버전 및 부 버전 증가)
	private String generateNextVersion(String currentVersion) {
		String[] versionParts = currentVersion.split("\\.");
		int major = Integer.parseInt(versionParts[0].substring(1));  // "v1" -> 1
		int minor = Integer.parseInt(versionParts[1]);

		// 부 버전 증가
		minor++;

		return "v" + major + "." + minor;
	}
}
