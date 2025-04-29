package com.househub.backend.domain.consultation.dto;

import java.time.LocalDateTime;

import com.househub.backend.domain.consultation.enums.ConsultationStatus;
import com.househub.backend.domain.consultation.enums.ConsultationType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UpdateConsultationReqDto {
	@NotNull(message = "공인중개사 id를 입력하세요.")
	private Long agentId;

	private ConsultationType consultationType; // 상담 수단 변경 시 사용

	private String content;  // 상담 내용 수정 시 사용

	private LocalDateTime consultationDate;  // 상담 날짜 수정 시 사용

	private ConsultationStatus status;  // 상담 상태 변경 시 사용
}
