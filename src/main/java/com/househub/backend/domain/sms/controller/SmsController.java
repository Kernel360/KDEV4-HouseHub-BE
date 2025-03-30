package com.househub.backend.domain.sms.controller;

import com.househub.backend.domain.sms.dto.SmsRequestDto;
import com.househub.backend.domain.sms.service.SmsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private final SmsService smsService;

    public SmsController(@Autowired SmsService smsService) {
        this.smsService = smsService;
    }

    /**
     * SMS 전송 API 엔드포인트
     * @param smsRequestDto 클라이언트로부터 전달받은 SMS 요청 데이터 (JSON 형식)
     * @return Aligo API 응답 (JSON 형식)
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String,Object>> SendSMS(@RequestBody @Valid SmsRequestDto smsRequestDto) {
        Map<String, Object> response = smsService.sendSms(smsRequestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Map<String, Object>>> getSmsHistory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) Integer limitDay
    ) {
        List<Map<String, Object>> history = smsService.getRecentMessages(page, size, startDate, limitDay);
        return ResponseEntity.ok(history);
    }
}