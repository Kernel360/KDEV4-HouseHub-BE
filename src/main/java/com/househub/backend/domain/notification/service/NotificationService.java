package com.househub.backend.domain.notification.service;

import org.springframework.stereotype.Service;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.notification.entity.Notification;
import com.househub.backend.domain.notification.enums.NotificationType;
import com.househub.backend.domain.notification.event.InquiryCreatedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
	private final NotificationStore notificationStore;
	private final NotificationReader notificationReader;
	private final NotificationExecutor notificationExecutor;

	public void sendInquiryNotification(InquiryCreatedEvent event) {
		Agent receiver = notificationReader.findReceiverById(event.getReceiverId());

		Notification notification = Notification.builder()
			.receiver(receiver)
			.url("/inquiries/" + event.getInquiryId() + "/answers")
			.content(event.getContent())
			.type(NotificationType.INQUIRY_CREATED)
			.isRead(false)
			.build();

		// 1. 저장
		Notification saved = notificationStore.create(notification);

		// 2. 실시간 전송
		notificationExecutor.send(saved);
	}
}
