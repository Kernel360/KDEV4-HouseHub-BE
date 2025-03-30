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
    @NotEmpty(message = "발신번호를 입력해주세요")
    private String sender;

    @NotEmpty(message = "수신번호를 입력해주세요")
    private String receiver;

    private String msg;

    private String title;
}
