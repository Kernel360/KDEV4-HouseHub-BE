package com.househub.backend.domain.notification.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.househub.backend.common.dto.PaginationDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationListResDto {
	private List<NotificationDto> content;
	private PaginationDto pagination;

	public static NotificationListResDto fromPage(Page<NotificationDto> page) {
		return NotificationListResDto.builder()
			.content(page.getContent())
			.pagination(PaginationDto.fromPage(page))
			.build();
	}
}
