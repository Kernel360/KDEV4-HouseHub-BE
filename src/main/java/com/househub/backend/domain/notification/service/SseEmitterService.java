package com.househub.backend.domain.notification.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.househub.backend.domain.notification.dto.NotificationDto;
import com.househub.backend.domain.notification.entity.Notification;
import com.househub.backend.domain.notification.repository.EmitterRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseEmitterService {
	private final EmitterRepository emitterRepository;
	private final Map<Long, Queue<Notification>> notificationQueue = new ConcurrentHashMap<>();

	private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 1시간

	// 알림을 클라이언트로 전달하는 메서드
	public void send(Notification notification) {
		Long receiverId = notification.getReceiver().getId();
		List<SseEmitter> emitters = emitterRepository.findAll(receiverId);

		if (!emitters.isEmpty()) {
			List<SseEmitter> deadEmitters = new LinkedList<>();

			for (SseEmitter emitter : emitters) {
				try {
					emitter.send(NotificationDto.from(notification));
				} catch (IOException e) {
					String message = e.getMessage();
					if (message != null && message.contains("Broken pipe")) {
						log.warn("Broken pipe detected. Removing emitter: receiverId={}", receiverId);
					} else {
						log.warn("Emitter send failed: receiverId={}, reason={}", receiverId, e.getMessage());
					}
					emitter.completeWithError(e);
					deadEmitters.add(emitter);
				}
			}

			// 죽은 emitter 제거
			for (SseEmitter dead : deadEmitters) {
				emitterRepository.delete(receiverId, dead);
			}
		} else {
			// emitter가 없을 경우 큐에 알림 저장
			enqueueNotification(receiverId, notification);
		}
	}

	// 클라이언트 연결을 위한 메서드
	public SseEmitter createEmitter(Long receiverId) {
		SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
		emitterRepository.save(receiverId, emitter);

		emitter.onCompletion(() -> {
			emitterRepository.delete(receiverId, emitter);
			log.info("SSE completed: receiverId={}", receiverId);
		});

		emitter.onTimeout(() -> {
			emitterRepository.delete(receiverId, emitter);
			log.info("SSE timeout: receiverId={}", receiverId);
			emitter.complete();
		});

		emitter.onError(t -> {
			emitterRepository.delete(receiverId, emitter);
			log.info("SSE error: receiverId={}, message={}", receiverId, t.getMessage());
			emitter.completeWithError(t);
		});

		// 큐에 쌓여 있던 알림 전송
		Queue<Notification> pending = notificationQueue.remove(receiverId);
		if (pending != null) {
			for (Notification n : pending) {
				try {
					emitter.send(NotificationDto.from(n));
				} catch (IOException e) {
					log.warn("Failed to send queued notification to receiverId={}", receiverId);
				}
			}
		}

		return emitter;
	}

	// 클라이언트 연결 종료 메서드
	public void removeEmitter(Long receiverId) {
		emitterRepository.deleteAll(receiverId);
	}

	private void enqueueNotification(Long receiverId, Notification notification) {
		notificationQueue.computeIfAbsent(receiverId, id -> new LinkedList<>()).add(notification);
	}
}
