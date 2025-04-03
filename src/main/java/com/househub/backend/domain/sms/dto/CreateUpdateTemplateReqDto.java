package com.househub.backend.domain.sms.dto;

import com.househub.backend.domain.agent.entity.RealEstate;
import com.househub.backend.domain.sms.entity.SmsTemplate;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateTemplateReqDto {

	@NotBlank(message = "템플릿 제목은 필수입니다")
	private String title;

	@NotBlank(message = "템플릿 내용은 필수입니다")
	private String content;

	public SmsTemplate toEntity(RealEstate realEstate) {
		return SmsTemplate.builder()
			.title(this.title)
			.content(this.content)
			.realEstate(realEstate)
			.build();
	}
}
