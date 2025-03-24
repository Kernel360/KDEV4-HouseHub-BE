package com.househub.backend.domain.property.dto;

public class ResponseDto {

    public static class PostResponse {
        public Long propertyId;
        public String message;

        public PostResponse(Long propertyId) {
            this.propertyId = propertyId;
            message = "매물이 성공적으로 등록되었습니다.";
        }
    }
}
