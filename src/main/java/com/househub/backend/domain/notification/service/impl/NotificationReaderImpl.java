package com.househub.backend.domain.notification.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.service.AgentReader;
import com.househub.backend.domain.notification.entity.Notification;
import com.househub.backend.domain.notification.repository.NotificationRepository;
import com.househub.backend.domain.notification.service.NotificationReader;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationReaderImpl implements NotificationReader {
	private final AgentReader agentReader;
	private final NotificationRepository notificationRepository;

	@Override
	public List<Notification> findUnreadNotifications(Long receiverId) {
		return notificationRepository.findAllByReceiverIdAndIsReadFalse(receiverId);
	}

	public Agent findReceiverById(Long receiverId) {
		return agentReader.findById(receiverId);
	}
}
