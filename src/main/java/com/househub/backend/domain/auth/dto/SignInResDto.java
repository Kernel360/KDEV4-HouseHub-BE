package com.househub.backend.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class SignInResDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;
}
