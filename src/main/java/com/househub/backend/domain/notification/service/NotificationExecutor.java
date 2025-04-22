package com.househub.backend.domain.notification.service;

import com.househub.backend.domain.notification.entity.Notification;

public interface NotificationExecutor {
	void send(Notification notification);
}
