package com.househub.backend.domain.sms.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 알 수 없는 필드 무시
public class AligoHistoryResDto {

	@JsonProperty("result_code") // API 응답 필드명과 매핑
	private Integer resultCode;

	private String message;

	@JsonProperty("list")
	private List<HistoryDetailDto> list;

	@JsonProperty("next_yn")
	private String nextYn;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class HistoryDetailDto {
		@JsonProperty("mid") // API 필드명이 snake_case인 경우 명시적 매핑
		private String mid;  // API에서 문자열로 오는 경우 대비 String 타입 변경

		@JsonProperty("type")
		private String msgType; // type -> msgType으로 의미있는 이름 변경

		private String sender;

		@JsonProperty("sms_count")
		private Integer smsCount;

		@JsonProperty("reserve_state")
		private String reserveState;

		private String msg;

		@JsonProperty("fail_count")
		private Integer failCount;

		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 실제 API 응답 형식에 맞춤
		@JsonProperty("reg_date")
		private LocalDateTime regDate;

		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		@JsonProperty("reserve") // API 필드명 확인 필요(예: reserve_date?)
		private LocalDateTime reserve;
	}
}
