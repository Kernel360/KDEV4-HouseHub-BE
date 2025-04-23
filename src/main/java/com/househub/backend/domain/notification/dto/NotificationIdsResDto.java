package com.househub.backend.domain.notification.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationIdsResDto {
	private List<Long> notificationIds;
}

