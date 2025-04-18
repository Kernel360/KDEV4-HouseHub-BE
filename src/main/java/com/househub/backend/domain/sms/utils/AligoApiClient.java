package com.househub.backend.domain.sms.utils;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.househub.backend.domain.sms.dto.AligoHistoryResDto;
import com.househub.backend.domain.sms.dto.AligoSmsResDto;
import com.househub.backend.domain.sms.dto.SendSmsReqDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AligoApiClient {

	private final RestTemplate restTemplate;
	@Value("${aligo.apikey}")
	private String apiKey;
	@Value("${aligo.id}")
	private String userId;

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
		params.add("testmode_yn", "Y");

		try {
			ResponseEntity<String> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				new HttpEntity<>(params, headers),
				String.class
			);

			// JSON 응답 파싱
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception e) {
			log.error("Aligo API 호출 실패: {}", e.getMessage());
			throw new RuntimeException("Aligo API 호출 중 오류 발생");
		}
	}

	public <T> T sendRequestForObject(String url, MultiValueMap<String, String> params, Class<T> responseType) {
		Map<String, Object> rawResponse = sendRequest(url, params);

		try {
			ObjectMapper mapper = new ObjectMapper();
			// JavaTimeModule 등록
			mapper.registerModule(new JavaTimeModule());
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

			return mapper.convertValue(rawResponse, responseType);
		} catch (Exception e) {
			log.error("응답 변환 중 오류 발생: {}", e.getMessage(), e);
			throw new RuntimeException("API 응답 변환 중 오류 발생", e);
		}
	}

	public AligoSmsResDto sendSms(SendSmsReqDto request) {
		String url = "https://apis.aligo.in/send/";

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("sender", request.getSender());
		params.add("receiver", request.getReceiver());
		params.add("msg", request.getMsg());

		// LMS/MMS 처리
		if (request.getMsg().length() > 90 || request.getTitle() != null) {
			params.add("title", request.getTitle());
			params.add("msg_type", "LMS");
		}

		// 선택적 파라미터 추가
		addIfNotNull(params, "rdate", request.getRdate());
		addIfNotNull(params, "rtime", request.getRtime());

		return sendRequestForObject(url, params, AligoSmsResDto.class);
	}

	private void addIfNotNull(MultiValueMap<String, String> params, String key, String value) {
		if (value != null) {
			params.add(key, value);
		}
	}

	public AligoHistoryResDto getRecentMessages(Integer page, Integer pageSize, String startDate, Integer limitDay){
		String url = "https://apis.aligo.in/list/";

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		if (page != null)
			params.add("page", page.toString());
		if (pageSize != null)
			params.add("page_size", pageSize.toString());
		if (startDate != null)
			params.add("start_date", startDate);
		if (limitDay != null)
			params.add("limit_day", limitDay.toString());

		return sendRequestForObject(url, params, AligoHistoryResDto.class);
	}


}

