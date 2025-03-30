package com.househub.backend.domain.sms.service;

import com.househub.backend.domain.sms.dto.SmsRequestDto;
import com.househub.backend.domain.sms.utils.AligoApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AligoSmsServiceImpl implements SmsService {

    private final AligoApiClient aligoApiClient;

    public Map<String, Object> sendSms(SmsRequestDto request) {
        String url = "https://apis.aligo.in/send/";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("sender", request.getSender());
        params.add("receiver", request.getReceiver());
        params.add("msg", request.getMsg());
        params.add("testmode_yn", "Y");

        // LMS/MMS 처리
        if (request.getMsg().length() > 90 || request.getTitle() != null) {
            params.add("title", request.getTitle());
            params.add("msg_type", "LMS");
        }

        return aligoApiClient.sendRequest(url, params);
    }


    /**
     * 전송 내역 조회
     * @param page 페이지 번호 (선택)
     * @param pageSize 페이지당 항목 수 (선택)
     * @param startDate 조회 시작 날짜 (YYYYMMDD 형식, 선택)
     * @param limitDay 조회 제한 일수 (선택)
     * @return 전송 내역 리스트
     */
    public List<Map<String, Object>> getRecentMessages(Integer page, Integer pageSize, String startDate, Integer limitDay) {
        String url = "https://apis.aligo.in/list/";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (page != null) params.add("page", page.toString());
        if (pageSize != null) params.add("page_size", pageSize.toString());
        if (startDate != null) params.add("start_date", startDate);
        if (limitDay != null) params.add("limit_day", limitDay.toString());

        Map<String, Object> response = aligoApiClient.sendRequest(url, params);

        if ("1".equals(response.get("result_code").toString())) {
            return (List<Map<String, Object>>) response.get("list");
        } else {
            return Collections.emptyList();
        }
    }
}
