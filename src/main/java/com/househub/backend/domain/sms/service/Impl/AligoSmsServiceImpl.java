package com.househub.backend.domain.sms.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.househub.backend.domain.sms.dto.SendSmsReqDto;
import com.househub.backend.domain.sms.dto.SmsHistoryResDto;
import com.househub.backend.domain.sms.dto.SmsResDto;
import com.househub.backend.domain.sms.service.SmsService;
import com.househub.backend.domain.sms.utils.AligoApiClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AligoSmsServiceImpl implements SmsService {

	private final AligoApiClient aligoApiClient;

	public SmsResDto sendSms(SendSmsReqDto request) {
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
		addIfNotNull(params, "destination", request.getDestination());
		addIfNotNull(params, "rdate", request.getRdate());
		addIfNotNull(params, "rtime", request.getRtime());
		addIfNotNull(params, "image1", request.getImage1());
		addIfNotNull(params, "image2", request.getImage2());
		addIfNotNull(params, "image3", request.getImage3());

		return aligoApiClient.sendRequestForObject(url, params, SmsResDto.class);
	}

	private void addIfNotNull(MultiValueMap<String, String> params, String key, String value) {
		if (value != null) {
			params.add(key, value);
		}
	}

	/**
	 * 전송 내역 조회
	 *
	 * @param page      페이지 번호 (선택)
	 * @param pageSize  페이지당 항목 수 (선택)
	 * @param startDate 조회 시작 날짜 (YYYYMMDD 형식, 선택)
	 * @param limitDay  조회 제한 일수 (선택)
	 * @return 전송 내역 리스트
	 */
	public List<SmsHistoryResDto.HistoryDetailDto> getRecentMessages(Integer page, Integer pageSize, String startDate,
		Integer limitDay) {
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

		// 전체 응답을 HistoryResDto로 변환
		SmsHistoryResDto response = aligoApiClient.sendRequestForObject(url, params, SmsHistoryResDto.class);

		if (response.getResultCode() == 1) {
			return response.getList();
		} else {
			throw new RuntimeException("전송 내역 조회 실패: " + response.getMessage());
		}
	}

}