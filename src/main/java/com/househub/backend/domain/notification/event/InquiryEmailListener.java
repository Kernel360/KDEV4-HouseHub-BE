package com.househub.backend.domain.notification.event;

import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.househub.backend.domain.notification.dto.EmailReqDto;
import com.househub.backend.domain.notification.service.EmailService;
import com.househub.backend.domain.notification.service.impl.NewInquiryCreatedContentProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class InquiryEmailListener {
	private final EmailService emailService;
	private final NewInquiryCreatedContentProvider contentProvider;

	@Async("asyncExecutor")
	@EventListener
	public void sendEmail(InquiryNotificationReadyEvent event) {
		// --- 1. 문의 URL 링크 조립 ---
		String inquiryUrl = event.getNotification().getUrl();
		Map<String, String> variables = Map.of("inquiryUrl", inquiryUrl);

		// --- 2. 이메일 제목/본문 생성 ---
		String subject = contentProvider.getSubject();
		String body = contentProvider.getBody(variables);

		// --- 3. EmailReqDto 조립 & 전송 ---
		EmailReqDto emailRequest = EmailReqDto.builder()
			.to(event.getNotification().getReceiver().getEmail())
			.subject(subject)
			.body(body)
			.build();

		log.info("[EMAIL] 이메일 알림 전송 시작: {}", event);
		// --- 4. 이메일 전송 ---
		emailService.sendEmailSync(emailRequest);
		log.info("[EMAIL] 이메일 알림 전송 완료");
	}
}
