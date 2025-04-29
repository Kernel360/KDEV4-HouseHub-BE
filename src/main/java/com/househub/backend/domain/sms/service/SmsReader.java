package com.househub.backend.domain.sms.service;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.sms.entity.Sms;
import com.househub.backend.domain.sms.repository.SmsRepository;

public interface SmsReader {
	Page<Sms> findAllByKeyword(Long agentId, String receiver, String msg,Long templateId ,Pageable pageable);
	Sms findById(Long id, Long agentId);
	List<Sms> findFailLogsForResend();
}
