package com.househub.backend.domain.notification.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.notification.dto.NotificationDto;
import com.househub.backend.domain.notification.dto.NotificationListResDto;
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

	public NotificationListResDto findNotifications(Long receiverId, Boolean isRead, Pageable adjustedPageable) {
		Page<Notification> notifications = null;
		if (isRead == null) {
			notifications = notificationReader.findAll(receiverId, adjustedPageable);
		} else {
			notifications = notificationReader.findAllByReceiverIdAndIsRead(receiverId, isRead, adjustedPageable);
		}
		Page<NotificationDto> notificationDtos = notifications.map(NotificationDto::from);
		return NotificationListResDto.fromPage(notificationDtos);
	}
}
