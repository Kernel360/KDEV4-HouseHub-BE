package com.househub.backend.domain.sms.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
public class AligoApiClient {

    @Value("${aligo.apikey}")
    private String apiKey;

    @Value("${aligo.id}")
    private String userId;

    private final RestTemplate restTemplate;

    public AligoApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Aligo API 요청을 처리하는 공통 메서드
     * @param url API URL
     * @param params 요청 파라미터
     * @return JSON 형식의 응답 데이터
     */
    public Map<String, Object> sendRequest(String url, MultiValueMap<String, String> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 기본 파라미터 추가
        params.add("key", apiKey);
        params.add("user_id", userId);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(params, headers),
                    String.class
            );

            // JSON 응답 파싱
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            log.error("Aligo API 호출 실패: {}", e.getMessage());
            throw new RuntimeException("Aligo API 호출 중 오류 발생");
        }
    }
}

