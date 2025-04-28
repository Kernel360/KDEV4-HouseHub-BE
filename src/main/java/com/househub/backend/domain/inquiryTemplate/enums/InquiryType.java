package com.househub.backend.domain.inquiryTemplate.enums;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;

public enum InquiryType {
	BUY_APARTMENT("아파트_매수"),
	SELL_APARTMENT("아파트_매도"),
	RENT_APARTMENT("아파트_임대"),
	LEASE_APARTMENT("아파트_임차"),
	BUY_OFFICETEL("오피스텔_매수"),
	SELL_OFFICETEL("오피스텔_매도"),
	RENT_OFFICETEL("오피스텔_임대"),
	LEASE_OFFICETEL("오피스텔_임차"),
	BUY_COMMERCIAL("상가_매수"),
	SELL_COMMERCIAL("상가_매도"),
	RENT_COMMERCIAL("상가_임대"),
	LEASE_COMMERCIAL("상가_임차"),
	BUY_ONE_ROOM("원룸_매수"),
	SELL_ONE_ROOM("원룸_매도"),
	RENT_ONE_ROOM("원룸_임대"),
	LEASE_ONE_ROOM("원룸_임차"),
	BUY_TWO_ROOM("투룸_매수"),
	SELL_TWO_ROOM("투룸_매도"),
	RENT_TWO_ROOM("투룸_임대"),
	LEASE_TWO_ROOM("투룸_임차");

	private final String koreanName;

	InquiryType(String koreanName) {
		this.koreanName = koreanName;
	}

	public String getKoreanName() {
		return koreanName;
	}

	public static InquiryType fromKorean(String koreanName) {
		for (InquiryType type : InquiryType.values()) {
			if (type.getKoreanName().equals(koreanName)) {
				return type;
			}
		}
		throw new BusinessException(ErrorCode.INVALID_INQUIRY_TYPE);
	}
}

