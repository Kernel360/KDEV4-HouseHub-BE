package com.househub.backend.domain.sms.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateResDto {

	private Long id;
	private String title;
	private String content;
	private Long realEstateId;
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private LocalDateTime deletedAt;
}
