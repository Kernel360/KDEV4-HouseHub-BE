package com.househub.backend.domain.sms.dto;

import java.time.LocalDateTime;

import com.househub.backend.domain.sms.entity.SmsTemplate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateResDto {

	private Long id;
	private String title;
	private String content;
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private LocalDateTime deletedAt;

	public static TemplateResDto fromEntity(SmsTemplate template){
		return TemplateResDto.builder()
			.id(template.getId())
			.title(template.getTitle())
			.content(template.getContent())
			.createdAt(template.getCreatedAt())
			.updatedAt(template.getUpdatedAt())
			.deletedAt(template.getDeletedAt())
			.build();
	}
}
