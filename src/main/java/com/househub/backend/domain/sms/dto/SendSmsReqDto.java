package com.househub.backend.domain.sms.dto;

import com.househub.backend.domain.agent.entity.RealEstate;
import com.househub.backend.domain.sms.entity.Sms;
import com.househub.backend.domain.sms.enums.MessageType;
import com.househub.backend.domain.sms.enums.Status;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendSmsReqDto {

	@NotEmpty(message = "발신번호를 입력해주세요")
	@Pattern(regexp = "^[0-9]+$", message = "발신번호는 숫자만 입력 가능합니다")
	private String sender;

	@NotEmpty(message = "수신번호를 입력해주세요")
	@Pattern(regexp = "^[0-9]+$", message = "수신번호는 숫자만 입력 가능합니다")
	private String receiver;

	@NotEmpty(message = "발신 내용을 입력해주세요")
	private String msg;

	private MessageType msgType; // SMS, LMS, MMS 구분

	private String title; // 문자 제목 (LMS, MMS만 해당)

	@Pattern(regexp = "^\\d{8}$", message = "예약일은 YYYYMMDD 형식이어야 합니다")
	private String rdate; // 예약일 (YYYYMMDD)

	@Pattern(regexp = "^\\d{4}$", message = "예약시간은 HHII 형식이어야 합니다")
	private String rtime; // 예약시간 (HHII)

	public Sms toEntity(Status status, RealEstate realEstate) {
		return Sms.builder()
			.sender(this.sender)
			.receiver(this.receiver)
			.msg(this.msg)
			.msgType(this.msgType)
			.title(this.title)
			.status(status)
			.rdate(this.rdate)
			.rtime(this.rtime)
			.realEstate(realEstate)
			.build();
	}
}