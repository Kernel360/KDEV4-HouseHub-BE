package com.househub.backend.domain.notification.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailReqDto {
	private String to;
	private String subject;
	private String body;
}
