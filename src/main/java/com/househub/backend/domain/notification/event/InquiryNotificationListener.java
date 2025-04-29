package com.househub.backend.domain.notification.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.househub.backend.domain.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InquiryNotificationListener {
	private final NotificationService notificationService;

	@EventListener
	public void handleInquiryCreatedEvent(InquiryCreatedEvent event) {
		notificationService.sendInquiryNotification(event);
	}
}
