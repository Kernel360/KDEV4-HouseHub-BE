package com.househub.backend.domain.sms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.sms.dto.SmsTypeCountDto;
import com.househub.backend.domain.sms.entity.Sms;

public interface SmsReader {
	Page<Sms> findAllByKeyword(Long agentId, String receiver, String msg,Long templateId ,Pageable pageable);
	Page<Sms> findAllBySenderAndReceiver(String receiver, String sender, Pageable pageable);

	Sms findById(Long id, Long agentId);
	List<Sms> findFailLogsForResend();
	List<SmsTypeCountDto> countSmsByMessageType(Long agentId);
}
