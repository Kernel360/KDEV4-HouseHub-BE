package com.househub.backend.domain.sms.service;

import com.househub.backend.domain.sms.dto.SmsRequestDto;
import jakarta.validation.Valid;

public interface SmsService {

    void SendSms(@Valid SmsRequestDto smsRequestDto);
}
