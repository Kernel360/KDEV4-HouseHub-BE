package com.househub.backend.domain.sms.entity;

import java.time.LocalDateTime;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.sms.enums.MessageType;
import com.househub.backend.domain.sms.enums.SmsStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "sms_logs")
public class Sms {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String sender;

	private String receiver;

	private String msg;

	private MessageType msgType;

	private String title;

	private SmsStatus status;

	private String rdate; // 예약일 (YYYYMMDD)

	private String rtime; // 예약시간 (HHII)

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private LocalDateTime deletedAt;

	private Integer retryCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_id", nullable = false)
	private Agent agent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sms_template_id")
	private SmsTemplate smsTemplate;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public void updateStatus(SmsStatus status) {
		this.status = status;
	}

	public void incrementRetryCount() {
		this.retryCount = (retryCount == null) ? 1 : retryCount + 1;
	}
}
