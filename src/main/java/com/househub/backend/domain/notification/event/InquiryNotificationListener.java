package com.househub.backend.domain.notification.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.househub.backend.domain.notification.service.NotificationReader;
import com.househub.backend.domain.notification.service.NotificationStore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class InquiryNotificationListener {
	private final NotificationReader notificationReader;
	private final NotificationStore notificationStore;
	private final ApplicationEventPublisher eventPublisher;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handle(InquiryCreatedEvent event) {
		log.info("Handling InquiryCreatedEvent: {}", event);
		// Agent receiver = notificationReader.findReceiverById(event.getReceiverId());
		//
		// Notification notification = Notification.builder()
		// 	.receiver(receiver)
		// 	.url("/inquiries/" + event.getInquiryId() + "/answers")
		// 	.content(event.getContent())
		// 	.type(NotificationType.INQUIRY_CREATED)
		// 	.isRead(false)
		// 	.build();
		//
		// log.info("Creating notification: {}", notification);
		// // 1. 저장
		// Notification saved = notificationStore.create(notification);
		//
		// log.info("Notification saved: {}", saved);
		// 2. 여기에 Notification 포함된 이벤트 재발행
		eventPublisher.publishEvent(new InquiryNotificationReadyEvent(event.getNotification()));
		log.info("Published InquiryNotificationReadyEvent: {}", event.getNotification());
	}
}
