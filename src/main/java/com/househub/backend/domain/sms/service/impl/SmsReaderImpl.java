package com.househub.backend.domain.sms.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.sms.dto.SmsTypeCountDto;
import com.househub.backend.domain.sms.entity.Sms;
import com.househub.backend.domain.sms.enums.SmsStatus;
import com.househub.backend.domain.sms.repository.SmsRepository;
import com.househub.backend.domain.sms.service.SmsReader;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SmsReaderImpl implements SmsReader {

	private final SmsRepository smsRepository;

	@Override
	public Page<Sms> findAllByKeyword(Long agentId, String receiver, String msg,Long templateId, Pageable pageable) {
		receiver = receiver!=null?receiver.replace("-",""):null;
		return smsRepository.findAllSmsByAgentIdAndFiltersAndDeletedAtIsNull(agentId, receiver, msg, templateId, pageable);
	}

	@Override
	public Page<Sms> findAllBySenderAndReceiver(String receiver, String sender, Pageable pageable) {
		return smsRepository.findAllSmsBySenderAndReceiverAndDeletedAtIsNull(sender, receiver, pageable);
	}

	@Override
	public Sms findById(Long id, Long agentId) {
		return smsRepository.findByIdAndAgentId(id, agentId);
	}

	@Override
	public List<Sms> findFailLogsForResend() {
		return smsRepository.findSmsByStatus(SmsStatus.FAIL);
	}

	@Override
	public List<SmsTypeCountDto> countSmsByMessageType(Long agentId) {
		LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
		return smsRepository.countSmsByMessageType(agentId, startOfMonth);
	}
}
