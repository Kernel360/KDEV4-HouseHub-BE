package com.househub.backend.domain.property.dto;

import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PropertyReqDto {
    @NotNull
    private Long customerId;
    @NotNull
    private PropertyType propertyType;
    private String memo;
    @NotNull
    private String roadAddress; // 도로명 주소
    @NotNull
    private String jibunAddress; // 지번 주소
    @NotNull
    private String detailAddress; // 상세 주소

    private BigDecimal latitude;
    private BigDecimal longitude;

    public Property toEntity(Customer customer) {
        Property property = Property.builder()
                .customer(customer)
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
