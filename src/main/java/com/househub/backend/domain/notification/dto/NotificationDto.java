package com.househub.backend.domain.notification.dto;

import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.househub.backend.domain.notification.entity.Notification;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationDto {
	private Long id;
	private Long receiverId;
	private String type;
	private String content;
	private String url;
	private String createdAt;
	@JsonProperty("isRead")
	private Boolean isRead;

	public static NotificationDto from(Notification notification) {
		return NotificationDto.builder()
			.id(notification.getId())
			.receiverId(notification.getReceiver().getId())
			.type(notification.getType().name())
			.content(notification.getContent())
			.url(notification.getUrl())
			.isRead(notification.getIsRead())
			.createdAt(notification.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
			.build();
	}
}

