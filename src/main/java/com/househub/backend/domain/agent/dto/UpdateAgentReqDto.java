package com.househub.backend.domain.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAgentReqDto {
	private String name;
	private String email;
	private String contact;
	private String licenseNumber;
	private Long birthdayTemplateId;
}
