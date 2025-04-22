package com.househub.backend.domain.notification.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.notification.entity.Notification;

public interface NotificationReader {
	Page<Notification> findAll(Long receiverId, Pageable pageable);

	Page<Notification> findAllByReceiverIdAndIsRead(Long receiverId, Boolean isRead, Pageable pageable);

	Agent findReceiverById(Long receiverId);
}
