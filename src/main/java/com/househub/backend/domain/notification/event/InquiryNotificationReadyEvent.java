package com.househub.backend.domain.notification.event;

import com.househub.backend.domain.notification.entity.Notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InquiryNotificationReadyEvent {

	private final Notification notification;
}

