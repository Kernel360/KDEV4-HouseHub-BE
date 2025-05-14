package com.househub.backend.domain.notification.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
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
	public List<Notification> findAll(Long receiverId) {
		return notificationRepository.findAllByReceiverIdAndNotDeleted(receiverId);
	}

	@Override
	public Page<Notification> findAll(Long receiverId, Pageable pageable) {
		return notificationRepository.findAllByReceiverIdAndNotDeleted(receiverId, pageable);
	}

	@Override
	public List<Notification> findAllByReceiverIdAndIsRead(Long receiverId, Boolean isRead) {
		return notificationRepository.findAllByReceiverIdAndIsReadAndNotDeleted(receiverId, isRead);
	}

	@Override
	public Page<Notification> findAllByReceiverIdAndIsRead(Long receiverId, Boolean isRead, Pageable pageable) {
		return notificationRepository.findAllByReceiverIdAndIsReadAndNotDeleted(receiverId, isRead, pageable);
	}

	@Override
	public List<Notification> findUnreadNotifications(Long receiverId) {
		return notificationRepository.findAllByReceiverIdAndIsReadAndNotDeletedOrderByCreatedAtDesc(receiverId, false);
	}

	@Override
	public Notification findById(Long notificationId) {
		return notificationRepository.findById(notificationId)
			.orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
	}

	@Override
	public List<Notification> findAllByIds(List<Long> ids) {
		return notificationRepository.findAllByIdInAndDeletedAtIsNull(ids);
	}

	@Override
	public List<Notification> findAllByReceiverIdAndIds(Long receiverId, List<Long> ids) {
		return notificationRepository.findAllByReceiverIdAndIdsAndNotDeleted(receiverId, ids);
	}

	public Agent findReceiverById(Long receiverId) {
		return agentReader.findById(receiverId);
	}
}
