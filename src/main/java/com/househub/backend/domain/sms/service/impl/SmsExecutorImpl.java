package com.househub.backend.domain.sms.service.impl;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.sms.dto.AligoSmsResDto;
import com.househub.backend.domain.sms.dto.SendSmsReqDto;
import com.househub.backend.domain.sms.entity.Sms;
import com.househub.backend.domain.sms.service.AligoGateway;
import com.househub.backend.domain.sms.service.SmsExecutor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SmsExecutorImpl implements SmsExecutor {
	private final AligoGateway aligoGateway;

	@Override
	public boolean resend(Sms log) {
		AligoSmsResDto result = aligoGateway.sendApiRequest(SendSmsReqDto.fromEntity(log));
		return result.getResultCode() == 1;
	}

	@Override
	public boolean sendNew(SendSmsReqDto request) {
		AligoSmsResDto result = aligoGateway.sendApiRequest(request);
		return result.getResultCode() == 1;
	}

}
