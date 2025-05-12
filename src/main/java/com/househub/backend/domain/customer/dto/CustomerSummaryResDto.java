package com.househub.backend.domain.customer.dto;

import com.househub.backend.domain.customer.entity.Customer;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomerSummaryResDto {
    private Long id;
    private String name;
    private String contact;

    public static CustomerSummaryResDto fromEntity(Customer customer) {
        if(customer.getDeletedAt() != null) {
            return CustomerSummaryResDto.builder()
                    .id(null)
                    .name("삭제된 고객")
                    .contact("")
                    .build();
        }
        return CustomerSummaryResDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .contact(customer.getContact())
                .build();
    }
}
