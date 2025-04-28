package com.househub.backend.domain.sms.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.sms.dto.AligoSmsResDto;
import com.househub.backend.domain.sms.dto.SendSmsReqDto;
import com.househub.backend.domain.sms.entity.Sms;
import com.househub.backend.domain.sms.service.AligoService;
import com.househub.backend.domain.sms.service.SmsExecutor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SmsExecutorImpl implements SmsExecutor {

	private final AligoService aligoService;

	@Override
	public boolean resend(Sms log) {
		AligoSmsResDto result = aligoService.sendSms(SendSmsReqDto.fromEntity(log));
		return result.getResultCode() == 1;
	}

	// @Override
	// public Sms sendContractExpireNotification(Customer customer) {
	// 	return ;
	// }
}
