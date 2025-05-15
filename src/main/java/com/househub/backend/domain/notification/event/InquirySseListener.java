package com.househub.backend.domain.notification.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.househub.backend.domain.notification.service.SseEmitterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class InquirySseListener {
	private final SseEmitterService sseSender;

	@Async("asyncExecutor")
	@EventListener
	public void sendSSE(InquiryNotificationReadyEvent event) {
		log.info("[SSE] SSE 알림 전송 시작: {}", event);
		sseSender.send(event.getNotification());
		log.info("[SSE] SSE 알림 전송 완료");
	}
}
