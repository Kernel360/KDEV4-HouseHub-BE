package com.househub.backend.domain.region.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "regions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Region {

	@Id
	private Long code;

	private String name;

	private String province;  // 시도명
	private String city;  // 시군구명
	private String dong;  // 읍면동명
}

