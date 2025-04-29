package com.househub.backend.domain.sms.dto;

import java.time.LocalDateTime;

import com.househub.backend.domain.sms.entity.Sms;
import com.househub.backend.domain.sms.entity.SmsTemplate;
import com.househub.backend.domain.sms.enums.MessageType;
import com.househub.backend.domain.sms.enums.SmsStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendSmsResDto {

	private Long id;
	private String sender;
	private String receiver;
	private String msg;
	private MessageType msgType;
	private String title;
	private SmsStatus status;
	private String rdate;
	private String rtime;
	private TemplateResDto smsTemplate;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;

	public static SendSmsResDto fromEntity(Sms sms) {
		return SendSmsResDto.builder()
			.id(sms.getId())
			.sender(sms.getSender())
			.receiver(sms.getReceiver())
			.msg(sms.getMsg())
			.msgType(sms.getMsgType())
			.title(sms.getTitle())
			.status(sms.getStatus())
			.rdate(sms.getRdate())
			.rtime(sms.getRtime())
			.smsTemplate(TemplateResDto.fromEntity(sms.getSmsTemplate()))
			.createdAt(sms.getCreatedAt())
			.updatedAt(sms.getUpdatedAt())
			.deletedAt(sms.getDeletedAt())
			.build();
	}
}
