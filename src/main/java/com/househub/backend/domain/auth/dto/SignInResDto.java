package com.househub.backend.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInResDto {
    private Long id;
    private String email;
}
