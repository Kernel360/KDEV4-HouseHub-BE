package com.househub.backend.domain.sms.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequestDto {
    @NotEmpty(message = "전화번호를 입력해주세요")
    private String phoneNum;

    @NotEmpty(message = "이름을 입력해주세요")
    private String name;
}
