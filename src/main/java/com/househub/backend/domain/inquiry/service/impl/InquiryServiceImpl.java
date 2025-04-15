package com.househub.backend.domain.inquiry.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.AgentStatus;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.customer.domain.entity.Customer;
import com.househub.backend.domain.customer.domain.entity.CustomerCandidate;
import com.househub.backend.domain.customer.domain.repository.CustomerCandidateRepository;
import com.househub.backend.domain.customer.domain.repository.CustomerRepository;
import com.househub.backend.domain.inquiry.dto.CreateInquiryReqDto;
import com.househub.backend.domain.inquiry.dto.CreateInquiryResDto;
import com.househub.backend.domain.inquiry.dto.InquiryDetailResDto;
import com.househub.backend.domain.inquiry.dto.InquiryListItemResDto;
import com.househub.backend.domain.inquiry.dto.InquiryListResDto;
import com.househub.backend.domain.inquiry.entity.Inquiry;
import com.househub.backend.domain.inquiry.entity.InquiryAnswer;
import com.househub.backend.domain.inquiry.exception.InvalidAnswerFormatException;
import com.househub.backend.domain.inquiry.exception.QuestionNotFoundException;
import com.househub.backend.domain.inquiry.repository.InquiryAnswerRepository;
import com.househub.backend.domain.inquiry.repository.InquiryRepository;
import com.househub.backend.domain.inquiry.service.InquiryService;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplateSharedToken;
import com.househub.backend.domain.inquiryTemplate.entity.Question;
import com.househub.backend.domain.inquiryTemplate.repository.InquiryTemplateRepository;
import com.househub.backend.domain.inquiryTemplate.repository.InquiryTemplateSharedTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
	private final InquiryTemplateRepository templateRepository;
	private final InquiryTemplateSharedTokenRepository sharedTokenRepository;
	private final InquiryRepository inquiryRepository;
	private final InquiryAnswerRepository inquiryAnswerRepository;
	private final CustomerRepository customerRepository;
	private final CustomerCandidateRepository customerCandidateRepository;
	private final ObjectMapper objectMapper;

	private final AgentRepository agentRepository;

	/**
	 * 문의를 생성합니다.
	 *
	 * @param reqDto 문의 생성 요청 DTO
	 * @return 문의 생성 응답 DTO
	 */
	@Transactional
	@Override
	public CreateInquiryResDto createInquiry(CreateInquiryReqDto reqDto) {
		// templateToken 으로 InquiryTemplateShareToken 엔티티 조회
		InquiryTemplateSharedToken shareToken = sharedTokenRepository.findByShareTokenAndActiveTrue(
				reqDto.getTemplateToken())
			.orElseThrow(() -> new ResourceNotFoundException("유효하지 않은 문의 템플릿 링크입니다.", "TEMPLATE_NOT_FOUND"));

		InquiryTemplate template = shareToken.getTemplate();

		if (template.getQuestions().isEmpty()) {
			throw new ResourceNotFoundException("문의 템플릿에 질문이 존재하지 않습니다.", "TEMPLATE_QUESTION_NOT_FOUND");
		}

		// 문의 남긴 고객이 DB 에 존재하는지 확인
		Optional<Customer> optCustomer = customerRepository.findByEmailAndContact(reqDto.getEmail(), reqDto.getPhone());

		// 고객이 DB에 존재하지 않는 경우, 고객 후보로 저장
		// 고객 후보는 고객과 1:1 관계이므로, 고객이 존재하지 않는 경우에만 저장
		CustomerCandidate candidate = null;
		if (optCustomer.isEmpty()) {
			candidate = customerCandidateRepository
				.findByEmailAndContact(reqDto.getEmail(), reqDto.getPhone())
				.orElseGet(() -> customerCandidateRepository.save(
					CustomerCandidate.builder()
						.name(reqDto.getName())
						.email(reqDto.getEmail())
						.contact(reqDto.getPhone())
						.build()
				));
		}

		// 문의 생성
		Inquiry inquiry = inquiryRepository.save(
			Inquiry.builder()
				.template(template)
				.customer(optCustomer.orElse(null))
				.candidate(candidate)
				.build()
		);

		// 질문 목록 캐싱
		Map<Long, Question> questionMap = template.getQuestions().stream()
			.collect(Collectors.toMap(Question::getId, Function.identity()));

		// 답변 저장
		for (CreateInquiryReqDto.AnswerDto answerDto : reqDto.getAnswers()) {
			Question question = questionMap.get(answerDto.getQuestionId());

			if (question == null)
				throw new QuestionNotFoundException(answerDto.getQuestionId());

			String serializedAnswer;
			try {
				if (answerDto.getAnswerText() instanceof String) {
					serializedAnswer = (String)answerDto.getAnswerText();
				} else {
					serializedAnswer = objectMapper.writeValueAsString(answerDto.getAnswerText());
				}
			} catch (JsonProcessingException e) {
				throw new InvalidAnswerFormatException();
			}

			InquiryAnswer answer = InquiryAnswer.builder()
				.inquiry(inquiry)
				.question(question)
				.answer(serializedAnswer)
				.build();

			inquiryAnswerRepository.save(answer);
		}

		return CreateInquiryResDto.builder()
			.inquiryId(inquiry.getId())
			.build();
	}

	/**
	 * 중개사 ID로 문의 목록을 조회합니다.
	 * @param agentId 중개사 ID
	 * @param keyword 검색 키워드
	 * @param pageable 페이지네이션 정보
	 * @return 문의 목록 응답 DTO
	 */
	@Override
	public InquiryListResDto getInquiries(Long agentId, String keyword, Pageable pageable) {
		// 1. 중개사 ID로 중개사 존재 여부 확인
		findAgentById(agentId);

		// 2. 문의 목록 검색
		Page<Inquiry> inquiries = inquiryRepository.findInquiriesWithCustomerOrCandidate(agentId, keyword, pageable);

		// 3. 문의 목록을 InquiryListResDto로 변환
		Page<InquiryListItemResDto> dtoPage = inquiries.map(i -> {
			boolean hasCustomer = i.getCustomer() != null;

			return InquiryListItemResDto.builder()
				.inquiryId(i.getId())
				.customerType(hasCustomer ?
					InquiryListItemResDto.CustomerType.CUSTOMER :
					InquiryListItemResDto.CustomerType.CUSTOMER_CANDIDATE)
				.name(hasCustomer ? i.getCustomer().getName() : i.getCandidate().getName())
				.email(hasCustomer ? i.getCustomer().getEmail() : i.getCandidate().getEmail())
				.contact(hasCustomer ? i.getCustomer().getContact() : i.getCandidate().getContact())
				.createdAt(i.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.build();
		});

		return InquiryListResDto.fromPage(dtoPage);
	}

	@Override
	public InquiryDetailResDto getInquiryDetail(Long inquiryId) {
		Inquiry inquiry = inquiryRepository.findWithDetailsById(inquiryId)
			.orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

		return InquiryDetailResDto.fromEntity(inquiry);
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
