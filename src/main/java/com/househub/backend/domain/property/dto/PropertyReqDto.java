package com.househub.backend.domain.property.dto;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyReqDto {
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

	private BigDecimal latitude;
	private BigDecimal longitude;

	public Property toEntity(Customer customer, Agent agent) {
		Property property = Property.builder()
			.customer(customer)
			.agent(agent)
			.propertyType(this.propertyType)
			.memo(this.memo)
			.roadAddress(this.roadAddress)
			.detailAddress(this.detailAddress)
			.latitude(this.latitude)
			.longitude(this.longitude)
			.build();
		property.parseJibunAddress(this.jibunAddress);
		return property;
	}
}
