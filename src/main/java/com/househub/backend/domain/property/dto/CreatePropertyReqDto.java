package com.househub.backend.domain.property.dto;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyDirection;
import com.househub.backend.domain.property.enums.PropertyType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePropertyReqDto {
	@NotNull(message = "의뢰인을 설정해 주세요.")
	private Long customerId;
	@NotNull(message = "매물 유형은 필수입니다.")
	private PropertyType propertyType;
	private String memo;
	@NotNull(message = "도로명 주소는 필수입니다.")
	private String roadAddress; // 도로명 주소
	@NotNull(message = "지번 주소는 필수입니다.")
	private String jibunAddress; // 지번 주소
	@NotNull(message = "상세 주소는 필수입니다.")
	private String detailAddress; // 상세 주소

	private Double area; // 면적 (평수)
	private Integer floor; // 층수
	private Integer allFloors; // 총 층수
	private PropertyDirection direction; // 방향 (남, 북, 동, 서 , ...)
	private Integer bathroomCnt; // 욕실 개수
	private Integer roomCnt; // 방 개수
	private Boolean active; // 매물이 계약 가능한지 여부
	private List<Long> tagIds;

	public Property toEntity(Customer customer, Agent agent) {
		Property property = Property.builder()
			.customer(customer)
			.agent(agent)
			.propertyType(this.propertyType)
			.memo(this.memo)
			.roadAddress(this.roadAddress)
			.detailAddress(this.detailAddress)
			.jibunAddress(this.jibunAddress)
			.area(this.area)
			.floor(this.floor)
			.allFloors(this.allFloors)
			.direction(this.direction)
			.bathroomCnt(this.bathroomCnt)
			.roomCnt(this.roomCnt)
			.active(this.active)
			.build();
		property.parseJibunAddress(this.jibunAddress);
		return property;
	}
}
