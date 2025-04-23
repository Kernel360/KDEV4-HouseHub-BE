package com.househub.backend.domain.notification.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.notification.entity.Notification;

public interface NotificationReader {
	List<Notification> findAll(Long receiverId);

	Page<Notification> findAll(Long receiverId, Pageable pageable);

	List<Notification> findAllByReceiverIdAndIsRead(Long receiverId, Boolean isRead);

	Page<Notification> findAllByReceiverIdAndIsRead(Long receiverId, Boolean isRead, Pageable pageable);

	List<Notification> findAllByIds(List<Long> ids);

	List<Notification> findAllByReceiverIdAndIds(Long receiverId, List<Long> ids);

	Agent findReceiverById(Long receiverId);

	Notification findById(Long notificationId);
}
