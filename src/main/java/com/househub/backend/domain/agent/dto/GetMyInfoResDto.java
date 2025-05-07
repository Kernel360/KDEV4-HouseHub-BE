package com.househub.backend.domain.agent.dto;

import java.time.LocalDateTime;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.enums.AgentStatus;
import com.househub.backend.domain.agent.entity.RealEstate;
import com.househub.backend.domain.agent.enums.Role;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetMyInfoResDto {
	private Long id;
	private String email;
	private String name;
	private String contact;
	private String licenseNumber;
	private Role role;
	private AgentStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private RealEstateResDto realEstate;

	public static GetMyInfoResDto from(Agent agent) {
		// 이 시점에서 RealEstate 엔티티가 데이터베이스에서 로딩됩니다.
		RealEstate realEstate = agent.getRealEstate();
		RealEstateResDto realEstateResDto = null;

		if (realEstate != null) {
			realEstateResDto = RealEstateResDto.builder()
				.id(realEstate.getId())
				.name(realEstate.getName())
				.businessRegistrationNumber(realEstate.getBusinessRegistrationNumber())
				.address(realEstate.getAddress())
				.roadAddress(realEstate.getRoadAddress())
				.contact(realEstate.getContact())
				.build();
		}

		return GetMyInfoResDto.builder()
			.id(agent.getId())
			.email(agent.getEmail())
			.name(agent.getName())
			.contact(agent.getContact())
			.licenseNumber(agent.getLicenseNumber())
			.role(agent.getRole())
			.status(agent.getStatus())
			.createdAt(agent.getCreatedAt())
			.updatedAt(agent.getUpdatedAt())
			.realEstate(realEstateResDto)
			.build();
	}

	@Getter
	@Builder
	public static class RealEstateResDto {
		private Long id;
		private String name;
		private String businessRegistrationNumber;
		private String address;
		private String roadAddress;
		private String contact;
	}
}