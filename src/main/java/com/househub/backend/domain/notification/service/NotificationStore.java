package com.househub.backend.domain.notification.service;

import com.househub.backend.domain.notification.entity.Notification;

public interface NotificationStore {
	Notification create(Notification notification);
}
