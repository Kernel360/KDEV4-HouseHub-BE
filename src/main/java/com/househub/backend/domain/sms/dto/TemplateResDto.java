package com.househub.backend.domain.sms.dto;

import java.time.LocalDateTime;

import com.househub.backend.domain.agent.entity.RealEstate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateResDto {

	private Long id;
	private String title;
	private String content;
	private RealEstate realEstate;
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private LocalDateTime deletedAt;
}
