package com.househub.backend.domain.notification.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.common.util.SecurityUtil;
import com.househub.backend.domain.notification.dto.NotificationDto;
import com.househub.backend.domain.notification.dto.NotificationIdsReqDto;
import com.househub.backend.domain.notification.dto.NotificationIdsResDto;
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

	/**
	 * 읽지 않은 알림 목록을 조회합니다.
	 * 사용자가 앱에 접속했을 때 SSE로 수신하지 못한 알림을 확인하는 용도입니다.
	 */
	@GetMapping("/unread")
	public ResponseEntity<SuccessResponse<List<NotificationDto>>> getUnreadNotifications() {
		List<NotificationDto> notifications = notificationService.getUnreadNotifications(
			SecurityUtil.getAuthenticatedAgent());
		return ResponseEntity.ok(SuccessResponse.success(
			"읽지 않은 알림 목록 조회 성공", "FIND_UNREAD_NOTIFICATIONS_SUCCESS", notifications));
	}

	@GetMapping
	public ResponseEntity<SuccessResponse<NotificationListResDto>> findNotifications(
		@RequestParam(name = "filter", defaultValue = "all") String filter,
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
			filter,
			adjustedPageable);
		return ResponseEntity.ok(
			SuccessResponse.success("알림 목록 조회 성공", "FIND_NOTIFICATIONS_SUCCESS", notifications));
	}

	@PostMapping("/read")
	public ResponseEntity<SuccessResponse<NotificationIdsResDto>> readNotifications(
		@RequestBody NotificationIdsReqDto request) {
		log.info("읽음 처리할 알림 ID 목록: {}", request.getNotificationIds());
		List<Long> processedIds;

		// 알림 ID 목록이 비어있을 경우 모든 알림을 읽음 처리
		if (request.isAll()) {
			Long receiverId = SecurityUtil.getAuthenticatedAgent().getId();
			processedIds = notificationService.readAllNotifications(receiverId);
			return ResponseEntity.ok(
				SuccessResponse.success("모든 알림 읽음 처리 성공", "READ_ALL_NOTIFICATIONS_SUCCESS",
					new NotificationIdsResDto(processedIds)));
		} else {
			processedIds = notificationService.readNotifications(request.getNotificationIds());
			return ResponseEntity.ok(
				SuccessResponse.success("선택한 알림 읽음 처리 성공", "READ_SELECTED_NOTIFICATIONS_SUCCESS",
					new NotificationIdsResDto(processedIds)));
		}
	}

	@PostMapping("/unread")
	public ResponseEntity<SuccessResponse<NotificationIdsResDto>> markNotificationsAsUnread(
		@RequestBody NotificationIdsReqDto request) {
		log.info("읽지 않음 처리할 알림 ID 목록: {}", request.getNotificationIds());
		List<Long> processedIds;

		// 알림 ID 목록이 비어있을 경우 모든 알림을 읽지 않음 처리
		if (request.isAll()) {
			Long receiverId = SecurityUtil.getAuthenticatedAgent().getId();
			processedIds = notificationService.markAllNotificationsAsUnread(receiverId);
			return ResponseEntity.ok(
				SuccessResponse.success("모든 알림 읽지 않음 처리 성공", "UNREAD_ALL_NOTIFICATIONS_SUCCESS",
					new NotificationIdsResDto(processedIds)));
		} else {
			processedIds = notificationService.markNotificationsAsUnread(request.getNotificationIds());
			return ResponseEntity.ok(
				SuccessResponse.success("선택한 알림 읽지 않음 처리 성공", "UNREAD_SELECTED_NOTIFICATIONS_SUCCESS",
					new NotificationIdsResDto(processedIds)));
		}
	}

	@PostMapping("/delete")
	public ResponseEntity<SuccessResponse<NotificationIdsResDto>> deleteNotifications(
		@RequestBody NotificationIdsReqDto request) {
		log.info("삭제할 알림 ID 목록: {}", request.getNotificationIds());

		List<Long> deletedIds;
		if (request.isAll()) {
			Long receiverId = SecurityUtil.getAuthenticatedAgent().getId();
			deletedIds = notificationService.deleteAllNotifications(receiverId);
		} else {
			Long receiverId = SecurityUtil.getAuthenticatedAgent().getId();
			deletedIds = notificationService.deleteNotifications(receiverId, request.getNotificationIds());
		}

		return ResponseEntity.ok(
			SuccessResponse.success("알림 삭제 성공", "DELETE_NOTIFICATION_SUCCESS", new NotificationIdsResDto(deletedIds)));
	}
}
