package com.househub.backend.domain.sms.service;

import com.househub.backend.domain.sms.dto.SendSmsReqDto;
import com.househub.backend.domain.sms.entity.Sms;

public interface SmsExecutor {
	boolean resend(Sms log);

	boolean sendNew(SendSmsReqDto request);
}
