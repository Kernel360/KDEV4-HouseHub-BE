package com.househub.backend.domain.notification.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.notification.entity.Notification;
import com.househub.backend.domain.notification.repository.NotificationRepository;
import com.househub.backend.domain.notification.service.NotificationStore;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationStoreImpl implements NotificationStore {
	private final NotificationRepository notificationRepository;

	@Override
	public Notification create(Notification notification) {
		return notificationRepository.save(notification);
	}
}
