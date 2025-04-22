package com.househub.backend.domain.notification.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.notification.entity.Notification;
import com.househub.backend.domain.notification.service.EmailService;
import com.househub.backend.domain.notification.service.NotificationExecutor;
import com.househub.backend.domain.notification.service.SseEmitterService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationExecutorImpl implements NotificationExecutor {
	private final EmailService emailService;
	private final SseEmitterService sseEmitterService;

	@Override
	public void send(Notification notification) {
		emailService.send(notification);
		sseEmitterService.send(notification);
	}
}
