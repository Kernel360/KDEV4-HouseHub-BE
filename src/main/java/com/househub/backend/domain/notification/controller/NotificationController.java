package com.househub.backend.domain.notification.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.househub.backend.common.util.SecurityUtil;
import com.househub.backend.domain.notification.service.SseEmitterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
	private final SseEmitterService sseEmitterService;

	@Operation(summary = "SSE 알림 스트리밍", description = "SSE를 통해 실시간 알림을 수신합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "SSE 연결 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "500", description = "서버 오류")
	})
	@GetMapping(value = "/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseBodyEmitter connectSse(
	) {
		Long receiverId = SecurityUtil.getAuthenticatedAgent().getId();
		log.info("수신인 정보: {}", receiverId);
		// 클라이언트가 연결을 요청하면 SseEmitter를 생성하고 반환
		SseEmitter emitter = sseEmitterService.createEmitter(receiverId);

		return emitter;
	}
}
