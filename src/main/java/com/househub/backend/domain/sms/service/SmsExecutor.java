package com.househub.backend.domain.sms.service;

import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.sms.entity.Sms;

public interface SmsExecutor {
	boolean resend(Sms log);

	// Sms sendContractExpire(Customer customer);
}
