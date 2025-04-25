package com.househub.backend.domain.region.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionOptionDto {
	private Long code;    // 고유 코드
	private String name;  // UI에 표시할 이름
}

