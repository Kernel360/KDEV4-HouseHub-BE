package com.househub.backend.common.exception;

public class NoAssociatedRealEstateException extends RuntimeException {
	public NoAssociatedRealEstateException() {
		super("소속된 부동산 정보가 없어 요청을 처리할 수 없습니다.");
	}
}

