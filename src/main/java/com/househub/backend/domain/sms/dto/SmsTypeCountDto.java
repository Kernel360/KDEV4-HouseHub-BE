package com.househub.backend.domain.sms.dto;

import com.househub.backend.domain.sms.enums.MessageType;

import lombok.Data;

@Data
public class SmsTypeCountDto {
	private MessageType messageType;
	private Long count;

	// 생성자 (JPQL NEW 표현식에 필수)
	public SmsTypeCountDto(MessageType messageType, Long count) {
		this.messageType = messageType;
		this.count = count;
	}
}