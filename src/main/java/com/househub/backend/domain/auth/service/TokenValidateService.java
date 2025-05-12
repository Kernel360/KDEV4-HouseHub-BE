package com.househub.backend.domain.auth.service;

import com.househub.backend.domain.auth.dto.ValidateTokenResDto;

public interface TokenValidateService {
	ValidateTokenResDto validate(String token, String type);
}
