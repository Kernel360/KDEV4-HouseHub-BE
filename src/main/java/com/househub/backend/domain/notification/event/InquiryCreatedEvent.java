package com.househub.backend.domain.notification.event;

import com.househub.backend.domain.notification.entity.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class InquiryCreatedEvent {
	private final Long receiverId;
	private final Long inquiryId;
	private final String content;
	private final Notification notification;
}
