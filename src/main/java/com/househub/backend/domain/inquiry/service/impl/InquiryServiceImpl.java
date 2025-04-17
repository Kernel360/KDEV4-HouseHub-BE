package com.househub.backend.domain.inquiry.service.impl;

import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.AgentStatus;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.service.CustomerExecutor;
import com.househub.backend.domain.inquiry.dto.CreateInquiryCommand;
import com.househub.backend.domain.inquiry.dto.CreateInquiryReqDto;
import com.househub.backend.domain.inquiry.dto.CreateInquiryResDto;
import com.househub.backend.domain.inquiry.dto.InquiryDetailResDto;
import com.househub.backend.domain.inquiry.dto.InquiryListItemResDto;
import com.househub.backend.domain.inquiry.dto.InquiryListResDto;
import com.househub.backend.domain.inquiry.entity.Inquiry;
import com.househub.backend.domain.inquiry.repository.InquiryRepository;
import com.househub.backend.domain.inquiry.service.InquiryExecutor;
import com.househub.backend.domain.inquiry.service.InquiryService;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.service.InquiryTemplateReader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
	private final CustomerExecutor customerExecutor;
	private final InquiryTemplateReader inquiryTemplateReader;
	private final InquiryExecutor inquiryExecutor;
	private final InquiryRepository inquiryRepository;
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
		InquiryTemplate template = inquiryTemplateReader.findByToken(reqDto.getTemplateToken());

		// 문의 남긴 고객이 DB 에 존재하는지 확인
		// 고객이 DB에 존재하지 않는 경우, 고객 새로 생성
		Customer customer = customerExecutor.findOrCreateCustomer(
			reqDto.toCustomerReqDto(),
			template.getAgent()
		);

		CreateInquiryCommand command = CreateInquiryCommand.builder()
			.template(template)
			.customer(customer)
			.answers(reqDto.getAnswers())
			.build();

		// executor 에 들어가는 기준
		// 일단 비즈니스 로직에서
		Inquiry inquiry = inquiryExecutor.executeInquiryCreation(command);

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
