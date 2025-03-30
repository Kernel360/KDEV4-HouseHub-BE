package com.househub.backend.domain.sms.service;

import com.househub.backend.domain.sms.dto.SmsRequestDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

public interface SmsService {

    Map<String, Object> sendSms(@Valid SmsRequestDto smsRequestDto);

    List<Map<String, Object>> getRecentMessages(Integer page, Integer pageSize, String startDate, Integer limitDay);
}
