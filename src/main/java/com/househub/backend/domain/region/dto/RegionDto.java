package com.househub.backend.domain.region.dto;

import java.io.Serializable;

import com.househub.backend.domain.region.entity.Region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionDto implements Serializable {
	private String code;    // 고유 코드
	private String name;  // UI에 표시할 이름

	public static RegionDto fromEntity(Region region) {
		return new RegionDto(region.getCode(), region.getName());
	}
}

