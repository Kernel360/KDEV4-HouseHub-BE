package com.househub.backend.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.util.StringUtils;

public enum Gender {
    M, F;

    @JsonCreator
    public static Gender fromString(String value) {
        if (value == null || value.isEmpty()) {
            return null; // 빈 문자열은 null로 처리
        }
        try {
            return Gender.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // 잘못된 값은 null로 처리
        }
    }
}
