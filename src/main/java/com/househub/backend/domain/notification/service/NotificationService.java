package com.househub.backend.domain.notification.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.notification.dto.NotificationDto;
import com.househub.backend.domain.notification.dto.NotificationListResDto;
import com.househub.backend.domain.notification.entity.Notification;
import com.househub.backend.domain.notification.enums.NotificationType;
import com.househub.backend.domain.notification.event.InquiryCreatedEvent;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	public NotificationListResDto findNotifications(Long receiverId, String filter, Pageable pageable) {
		Page<Notification> notifications = null;
		switch (filter.toLowerCase()) {
			case "all":
				notifications = notificationReader.findAll(receiverId, pageable);
				break;
			case "read":
				notifications = notificationReader.findAllByReceiverIdAndIsRead(receiverId, true, pageable);
				break;
			case "unread":
				notifications = notificationReader.findAllByReceiverIdAndIsRead(receiverId, false, pageable);
				break;
			default:
				throw new BusinessException(ErrorCode.INVALID_NOTIFICATION_FILTER);
		}
		Page<NotificationDto> notificationDtos = notifications.map(NotificationDto::from);
		return NotificationListResDto.fromPage(notificationDtos);
	}

	@Transactional
	public List<Long> readNotifications(List<Long> notificationIds) {
		List<Notification> notifications = notificationReader.findAllByIds(notificationIds);
		notifications.forEach(Notification::markAsRead);
		notificationStore.updateAll(notifications);
		return notificationIds;
	}

	@Transactional
	public List<Long> readAllNotifications(Long receiverId) {
		List<Notification> unreadNotifications = notificationReader.findAllByReceiverIdAndIsRead(receiverId, false);
		if (unreadNotifications.isEmpty()) {
			return List.of();
		}

		unreadNotifications.forEach(Notification::markAsRead);
		notificationStore.updateAll(unreadNotifications);
		return unreadNotifications.stream()
			.map(Notification::getId)
			.toList();
	}

	@Transactional
	public List<Long> markNotificationsAsUnread(List<Long> notificationIds) {
		List<Notification> notifications = notificationReader.findAllByIds(notificationIds);
		notifications.forEach(Notification::markAsUnread);
		notificationStore.updateAll(notifications);
		return notificationIds;
	}

	@Transactional
	public List<Long> markAllNotificationsAsUnread(Long receiverId) {
		List<Notification> readNotifications = notificationReader.findAllByReceiverIdAndIsRead(receiverId, true);
		if (readNotifications.isEmpty()) {
			return List.of();
		}

		readNotifications.forEach(Notification::markAsUnread);
		notificationStore.updateAll(readNotifications);
		return readNotifications.stream()
			.map(Notification::getId)
			.toList();
	}

	@Transactional
	public List<Long> deleteAllNotifications(Long receiverId) {
		List<Notification> notifications = notificationReader.findAll(receiverId);
		if (notifications.isEmpty()) {
			return List.of();
		}
		notifications.forEach(Notification::markAsDeleted);
		notificationStore.deleteAll(notifications);
		return notifications.stream()
			.map(Notification::getId)
			.toList();
	}

	@Transactional
	public List<Long> deleteNotifications(Long receiverId, List<Long> notificationIds) {
		List<Notification> notifications = notificationReader.findAllByReceiverIdAndIds(receiverId,
			notificationIds);
		notifications.forEach(Notification::markAsDeleted);
		notificationStore.deleteAll(notifications);
		return notifications.stream().map(Notification::getId).toList();
	}
}
