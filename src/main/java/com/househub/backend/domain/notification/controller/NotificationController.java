package com.househub.backend.domain.notification.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.common.util.SecurityUtil;
import com.househub.backend.domain.notification.dto.NotificationListResDto;
import com.househub.backend.domain.notification.service.NotificationService;
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
	private final NotificationService notificationService;

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

	@GetMapping
	public ResponseEntity<SuccessResponse<NotificationListResDto>> findUnreadNotifications(
		@RequestParam(required = false) Boolean isRead,
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		Long receiverId = SecurityUtil.getAuthenticatedAgent().getId();

		// 💡 page를 1-based에서 0-based로 변경
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int size = pageable.getPageSize();

		// 페이지네이션을 위한 Pageable 객체 생성
		Pageable adjustedPageable = PageRequest.of(page, size, pageable.getSort());

		NotificationListResDto notifications = notificationService.findNotifications(
			receiverId,
			isRead,
			adjustedPageable);
		return ResponseEntity.ok(
			SuccessResponse.success("아직 안 읽은 알림 목록 조회 성공", "FIND_UNREAD_NOTIFICATIONS_SUCCESS", notifications));
	}

}
