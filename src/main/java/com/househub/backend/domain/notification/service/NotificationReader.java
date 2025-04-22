package com.househub.backend.domain.notification.service;

import java.util.List;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.notification.entity.Notification;

public interface NotificationReader {
	List<Notification> findUnreadNotifications(Long receiverId);

	Agent findReceiverById(Long receiverId);
}
