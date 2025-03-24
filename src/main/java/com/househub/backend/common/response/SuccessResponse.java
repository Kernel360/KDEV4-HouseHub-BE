package com.househub.backend.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuccessResponse<T> {
    private boolean success;
    private String message;
    private String code;
    private T data;

    public static <T> SuccessResponse<T> success(String message, String code, T data) {
        return SuccessResponse.<T>builder()
                .success(true)
                .message(message)
                .code(code)
                .data(data)
                .build();
    }
}