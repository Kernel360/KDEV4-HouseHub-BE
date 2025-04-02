package com.househub.backend.domain.sms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSmsHistoryReqDto {

	@Min(value = 1, message = "페이지 번호는 최소 1 이상이어야 합니다")
	private int page = 1; // 기본값 1

	@Min(value = 1, message = "페이지 크기는 최소 1 이상이어야 합니다")
	private int size = 10; // 기본값 10

	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식은 YYYY-MM-DD이어야 합니다")
	private String startDate; // 필수가 아니므로 초기값 없음

	@Min(value = 0, message = "제한 일수는 최소 0 이상이어야 합니다")
	private Integer limitDay; // 필수가 아니므로 초기값 없음
}
