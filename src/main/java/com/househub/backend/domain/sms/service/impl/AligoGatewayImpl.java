package com.househub.backend.domain.sms.service.impl;

import java.util.List;
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
import com.househub.backend.domain.sms.dto.AligoHistoryResDto;
import com.househub.backend.domain.sms.dto.AligoSmsResDto;
import com.househub.backend.domain.sms.dto.SendSmsReqDto;
import com.househub.backend.domain.sms.service.AligoGateway;
import com.househub.backend.domain.sms.utils.MessageFormatter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AligoGatewayImpl implements AligoGateway {

	private final RestTemplate restTemplate;
	private final MessageFormatter formatter;

	@Value("${aligo.apikey}")
	private String apiKey;

	@Value("${aligo.id}")
	private String userId;

	@Value("${aligo.commonSender}")
	private String commonSender;

	private Map<String, Object> sendRequest(String url, MultiValueMap<String, String> params) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

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

			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(response.getBody(), new TypeReference<>() {});
		} catch (Exception e) {
			log.error("Aligo API 호출 실패: {}", e.getMessage());
			throw new RuntimeException("Aligo API 호출 중 오류 발생");
		}
	}

	private <T> T sendRequestForObject(String url, MultiValueMap<String, String> params, Class<T> responseType) {
		Map<String, Object> rawResponse = sendRequest(url, params);
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.convertValue(rawResponse, responseType);
		} catch (Exception e) {
			log.error("응답 변환 실패: {}", e.getMessage());
			throw new RuntimeException("API 응답 처리 오류");
		}
	}

	@Override
	public AligoSmsResDto addParamsAndSend(SendSmsReqDto request) {
		String url = "https://apis.aligo.in/send/";
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		params.add("sender", commonSender);
		params.add("receiver", request.getReceiver());
		params.add("msg", request.getMsg());

		if(request.getMsgType() != null) {
			params.add("msg_type", request.getMsgType().toString());
			if(request.getTitle() != null) params.add("title", request.getTitle());
		}

		addOptionalParam(params, "rdate", request.getRdate());
		addOptionalParam(params, "rtime", request.getRtime());

		return sendRequestForObject(url, params, AligoSmsResDto.class);
	}

	@Override
	public List<AligoHistoryResDto.HistoryDetailDto> getHistory(Integer page, Integer pageSize,
		String startDate, Integer limitDay) {
		String url = "https://apis.aligo.in/list/";
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		addOptionalParam(params, "page", page);
		addOptionalParam(params, "page_size", pageSize);
		addOptionalParam(params, "start_date", startDate);
		addOptionalParam(params, "limit_day", limitDay);

		AligoHistoryResDto response = sendRequestForObject(url, params, AligoHistoryResDto.class);
		if(response.getResultCode() == 1) {
			return response.getList();
		}
		throw new RuntimeException("히스토리 조회 실패: " + response.getMessage());
	}

	private void addOptionalParam(MultiValueMap<String, String> params, String key, Object value) {
		if(value != null) {
			params.add(key, value.toString());
		}
	}
}
