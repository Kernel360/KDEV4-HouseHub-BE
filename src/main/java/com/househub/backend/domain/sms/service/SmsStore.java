package com.househub.backend.domain.sms.service;

import com.househub.backend.domain.sms.entity.Sms;

public interface SmsStore {
	Sms createSms(Sms sms);
}
