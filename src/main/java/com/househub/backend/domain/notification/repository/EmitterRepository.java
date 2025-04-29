package com.househub.backend.domain.notification.repository;

import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {
	void save(Long userId, SseEmitter emitter);

	List<SseEmitter> findAll(Long userId);

	void delete(Long userId, SseEmitter emitter);

	void deleteAll(Long userId);
}
