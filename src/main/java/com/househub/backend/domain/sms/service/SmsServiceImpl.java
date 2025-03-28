package com.househub.backend.domain.sms.service;

import com.househub.backend.domain.sms.dto.SmsRequestDto;
import com.househub.backend.domain.sms.utils.SmsCertificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    private final SmsCertificationUtil smsCertificationUtil;

    //의존성 주입
    public SmsServiceImpl(@Autowired SmsCertificationUtil smsCertificationUtil) {
        this.smsCertificationUtil = smsCertificationUtil;
    }

    @Override // SmsService 인터페이스 메서드 구현
    public void SendSms(SmsRequestDto smsRequestDto) {
        String name = smsRequestDto.getName();
        String phoneNum = smsRequestDto.getPhoneNum();
        smsCertificationUtil.sendSMS(phoneNum, name); // SMS 인증 유틸리티를 사용하여 SMS 발송
    }
}