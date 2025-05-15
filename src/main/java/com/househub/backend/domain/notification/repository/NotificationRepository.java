package com.househub.backend.domain.notification.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.notification.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Query("SELECT n FROM Notification n WHERE n.receiver.id = :receiverId AND n.deletedAt IS NULL")
	List<Notification> findAllByReceiverIdAndNotDeleted(@Param("receiverId") Long receiverId);

	@Query("SELECT n FROM Notification n WHERE n.receiver.id = :receiverId AND n.deletedAt IS NULL")
	Page<Notification> findAllByReceiverIdAndNotDeleted(@Param("receiverId") Long receiverId, Pageable pageable);

	@Query("SELECT n FROM Notification n WHERE n.receiver.id = :receiverId AND n.isRead = :isRead AND n.deletedAt IS NULL")
	List<Notification> findAllByReceiverIdAndIsReadAndNotDeleted(@Param("receiverId") Long receiverId,
		@Param("isRead") Boolean isRead);

	@Query("SELECT n FROM Notification n WHERE n.receiver.id = :receiverId AND n.isRead = :isRead AND n.deletedAt IS NULL")
	Page<Notification> findAllByReceiverIdAndIsReadAndNotDeleted(@Param("receiverId") Long receiverId,
		@Param("isRead") Boolean isRead, Pageable pageable);

	@Query(
		"SELECT n FROM Notification n WHERE n.receiver.id = :receiverId AND n.isRead = :isRead AND n.deletedAt IS NULL "
			+
			"ORDER BY n.createdAt DESC")
	List<Notification> findAllByReceiverIdAndIsReadAndNotDeletedOrderByCreatedAtDesc(
		@Param("receiverId") Long receiverId,
		@Param("isRead") Boolean isRead);

	List<Notification> findAllByIdInAndDeletedAtIsNull(List<Long> ids);

	@Query("SELECT n FROM Notification n " +
		"WHERE n.receiver.id = :receiverId " +
		"AND n.id IN :ids " +
		"AND n.deletedAt IS NULL")
	List<Notification> findAllByReceiverIdAndIdsAndNotDeleted(@Param("receiverId") Long receiverId,
		@Param("ids") List<Long> ids);
}
