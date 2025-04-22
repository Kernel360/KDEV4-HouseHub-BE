package com.househub.backend.domain.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.notification.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	// receiverId 기준으로 전체 알림 조회
	Page<Notification> findAllByReceiver_Id(Long receiverId, Pageable pageable);

	// receiverId와 읽음 여부로 필터링된 알림 조회
	Page<Notification> findAllByReceiver_IdAndIsRead(Long receiverId, Boolean isRead, Pageable pageable);
}
