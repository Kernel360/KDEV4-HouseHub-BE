package com.househub.backend.domain.notification.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationIdsReqDto {
	private List<Long> notificationIds;
	@JsonProperty("isAll")
	private boolean all;
}

