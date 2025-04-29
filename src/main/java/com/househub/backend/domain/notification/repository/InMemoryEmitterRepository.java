package com.househub.backend.domain.notification.repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class InMemoryEmitterRepository implements EmitterRepository {
	private final Map<Long, List<SseEmitter>> emitterMap = new ConcurrentHashMap<>();

	@Override
	public void save(Long userId, SseEmitter emitter) {
		emitterMap.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);
	}

	@Override
	public List<SseEmitter> findAll(Long userId) {
		return emitterMap.getOrDefault(userId, List.of());
	}

	@Override
	public void delete(Long userId, SseEmitter emitter) {
		List<SseEmitter> emitters = emitterMap.get(userId);
		if (emitters != null) {
			emitters.remove(emitter);
			if (emitters.isEmpty()) {
				emitterMap.remove(userId);
			}
		}
	}

	@Override
	public void deleteAll(Long userId) {
		emitterMap.remove(userId);
	}
}
