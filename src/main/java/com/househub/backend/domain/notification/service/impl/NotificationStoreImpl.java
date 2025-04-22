package com.househub.backend.domain.notification.service.impl;

import java.util.List;

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

	@Override
	public void update(Notification notification) {
		notificationRepository.save(notification);
	}

	@Override
	public void updateAll(List<Notification> unreadNotifications) {
		notificationRepository.saveAll(unreadNotifications);
	}

	@Override
	public void delete(Notification notification) {
		notificationRepository.save(notification);
	}

	@Override
	public void deleteAll(List<Notification> notifications) {
		notificationRepository.saveAll(notifications);
	}
}
