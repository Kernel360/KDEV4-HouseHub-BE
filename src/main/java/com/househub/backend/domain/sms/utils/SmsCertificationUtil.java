package com.househub.backend.domain.sms.utils;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsCertificationUtil {

    DefaultMessageService messageService; // 메시지 서비스를 위한 객체
    @Value("${coolsms.apikey}") // coolsms의 API 키 주입
    private String apiKey;
    @Value("${coolsms.apisecret}") // coolsms의 API 비밀키 주입
    private String apiSecret;
    @Value("${coolsms.fromnumber}") // 발신자 번호 주입
    private String fromNumber;

    @PostConstruct // 의존성 주입이 완료된 후 초기화를 수행하는 메서드
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr"); // 메시지 서비스 초기화
    }

    // 단일 메시지 발송
    public void sendSMS(String to, String name) {
        Message message = new Message(); // 새 메시지 객체 생성
        message.setFrom(fromNumber); // 발신자 번호 설정
        message.setTo(to); // 수신자 번호 설정
        message.setText("환영합니다~ " + name + "님!"); // 메시지 내용 설정

        this.messageService.sendOne(new SingleMessageSendingRequest(message)); // 메시지 발송 요청
    }
}
