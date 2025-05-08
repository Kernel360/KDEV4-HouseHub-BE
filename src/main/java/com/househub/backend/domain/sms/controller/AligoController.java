package com.househub.backend.domain.sms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.sms.dto.AligoHistoryReqDto;
import com.househub.backend.domain.sms.dto.AligoHistoryResDto;
import com.househub.backend.domain.sms.service.AligoService;
import com.househub.backend.domain.sms.service.SmsService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/aligo")
public class AligoController {

	private final AligoService aligoService;

	@Operation(
		summary = "Aligo 문자 발송 이력 조회",
		description = "Aligo Api 서버에 보낸 문자 발송 이력을 조건에 따라 조회합니다. 날짜, 페이지, 건수 등으로 필터링할 수 있습니다."
	)
	@GetMapping("/history")
	public ResponseEntity<SuccessResponse<List<AligoHistoryResDto.HistoryDetailDto>>> getAligoSmsHistories(
		@ModelAttribute @Valid AligoHistoryReqDto dto
	) {
		List<AligoHistoryResDto.HistoryDetailDto> response = aligoService.getRecentMessages(dto.getPage(), dto.getSize(), dto.getStartDate(), dto.getLimitDay());
		return ResponseEntity.ok(SuccessResponse.success("문자 내역 조회에 성공했습니다.", "SMS_HISTORY_READ_SUCCESS", response));
	}

}
