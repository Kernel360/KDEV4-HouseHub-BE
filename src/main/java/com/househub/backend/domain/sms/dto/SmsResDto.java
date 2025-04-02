package com.househub.backend.domain.sms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true) // 알 수 없는 필드 무시
public class SmsResDto {

	@JsonProperty("result_code") // JSON 필드명과 매핑
	private Integer resultCode; // 결과코드(API 수신유무)

	private String message;     // 결과 메시지

	@JsonProperty("msg_id")
	private Integer msgId;      // 메시지 고유 ID

	@JsonProperty("success_cnt")
	private Integer successCnt; // 요청 성공 건수

	@JsonProperty("error_cnt")
	private Integer errorCnt;   // 요청 실패 건수

	@JsonProperty("msg_type")
	private String msgType;     // 메시지 타입 (SMS, LMS, MMS)
}