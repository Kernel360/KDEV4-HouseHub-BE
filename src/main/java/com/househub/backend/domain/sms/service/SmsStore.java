package com.househub.backend.domain.sms.service;

import java.util.List;

import com.househub.backend.domain.sms.entity.Sms;

public interface SmsStore {
	Sms create(Sms sms);
	void saveAll(List<? extends Sms> logs);
}
