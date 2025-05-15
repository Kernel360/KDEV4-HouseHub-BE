package com.househub.backend.domain.inquiry.service.impl;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.service.CustomerExecutor;
import com.househub.backend.domain.inquiry.dto.CreateInquiryCommand;
import com.househub.backend.domain.inquiry.dto.CreateInquiryReqDto;
import com.househub.backend.domain.inquiry.dto.CreateInquiryResDto;
import com.househub.backend.domain.inquiry.dto.InquiryDetailResDto;
import com.househub.backend.domain.inquiry.dto.InquiryListItemResDto;
import com.househub.backend.domain.inquiry.dto.InquiryListResDto;
import com.househub.backend.domain.inquiry.entity.Inquiry;
import com.househub.backend.domain.inquiry.service.InquiryExecutor;
import com.househub.backend.domain.inquiry.service.InquiryReader;
import com.househub.backend.domain.inquiry.service.InquiryService;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.service.InquiryTemplateReader;
import com.househub.backend.domain.notification.entity.Notification;
import com.househub.backend.domain.notification.enums.NotificationType;
import com.househub.backend.domain.notification.event.InquiryCreatedEvent;
import com.househub.backend.domain.notification.service.NotificationReader;
import com.househub.backend.domain.notification.service.NotificationStore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
	private final CustomerExecutor customerExecutor;
	private final InquiryTemplateReader inquiryTemplateReader;
	private final InquiryExecutor inquiryExecutor;
	private final InquiryReader inquiryReader;
	private final NotificationReader notificationReader;
	private final NotificationStore notificationStore;
	private final ApplicationEventPublisher eventPublisher;

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

		Inquiry inquiry = inquiryExecutor.executeInquiryCreation(command);

		String notificationContent = buildInquiryNotificationContent(reqDto);
		log.info("문의 알림 컨텐츠 내용: {}",
			notificationContent);

		Agent receiver = notificationReader.findReceiverById(template.getAgent().getId());

		Notification notification = Notification.builder()
			.receiver(receiver)
			.url("/inquiries/" + inquiry.getId() + "/answers")
			.content(notificationContent)
			.type(NotificationType.INQUIRY_CREATED)
			.isRead(false)
			.build();

		log.info("Creating notification: {}", notification);
		// 1. 저장
		Notification saved = notificationStore.create(notification);

		log.info("문의 알림 이벤트 발행");
		// 알림 이벤트 발행
		eventPublisher.publishEvent(
			InquiryCreatedEvent.builder()
				.receiverId(template.getAgent().getId())
				.inquiryId(inquiry.getId())
				.content(notificationContent)
				.notification(saved)
				.build()
		);

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
	@Transactional
	@Override
	public InquiryListResDto getInquiries(Long agentId, String keyword, Pageable pageable) {
		// 1. 문의 목록 검색
		Page<Inquiry> inquiries = inquiryReader.findPageByAgentIdAndKeyword(agentId, keyword, pageable);

		// 2. 문의 목록을 InquiryListResDto로 변환
		Page<InquiryListItemResDto> dtoPage = inquiries.map(InquiryListItemResDto::from);

		return InquiryListResDto.fromPage(dtoPage);
	}

	@Override
	public InquiryDetailResDto getInquiryDetail(Long inquiryId) {
		Inquiry inquiry = inquiryReader.findInquiryWithDetailsOrThrow(inquiryId);
		return InquiryDetailResDto.fromEntity(inquiry);
	}

	@Override
	public InquiryListResDto findAllByCustomer(Long id, Pageable pageable, Long agentId) {
		Page<Inquiry> inquiries = inquiryReader.findPageByAgentAndCustomerId(agentId, id, pageable);

		Page<InquiryListItemResDto> dtoPage = inquiries.map(InquiryListItemResDto::from);

		return InquiryListResDto.fromPage(dtoPage);
	}

	private String buildInquiryNotificationContent(CreateInquiryReqDto reqDto) {
		String phone = reqDto.getPhone();

		// 첫 번째 답변 → 문의 유형
		String inquiryType = reqDto.getAnswers().stream()
			.findFirst()
			.map(CreateInquiryReqDto.AnswerDto::getAnswerText)
			.map(Object::toString)
			.orElse("문의");

		return String.format("[%s] 고객님의 '%s' 문의가 접수되었습니다.", phone, inquiryType);
	}

}
