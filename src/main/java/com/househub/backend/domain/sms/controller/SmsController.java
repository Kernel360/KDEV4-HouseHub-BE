package com.househub.backend.domain.sms.controller;

import com.househub.backend.domain.sms.dto.SmsRequestDto;
import com.househub.backend.domain.sms.service.SmsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private final SmsService smsService;

    public SmsController(@Autowired SmsService smsService){
        this.smsService = smsService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> SendSMS(@RequestBody @Valid SmsRequestDto smsRequestDto){
        smsService.SendSms(smsRequestDto);
        return ResponseEntity.ok("문자를 전송했습니다.");
    }
}