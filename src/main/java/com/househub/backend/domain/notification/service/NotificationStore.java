package com.househub.backend.domain.notification.service;

import java.util.List;

import com.househub.backend.domain.notification.entity.Notification;

public interface NotificationStore {
	Notification create(Notification notification);

	void update(Notification notification);

	void updateAll(List<Notification> unreadNotifications);

	void delete(Notification notification);

	void deleteAll(List<Notification> notifications);
}
