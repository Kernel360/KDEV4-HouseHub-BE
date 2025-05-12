package com.househub.backend.domain.notification.service.impl;

import java.util.concurrent.CompletableFuture;

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
		CompletableFuture<Void> emailTask = CompletableFuture.runAsync(() -> emailService.send(notification));
		emailTask.thenRun(() -> sseEmitterService.send(notification));
	}
}
