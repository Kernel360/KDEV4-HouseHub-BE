package com.househub.backend.domain.customer.interfaces.dto;

import java.time.LocalDateTime;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.domain.customer.domain.entity.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerResDto{

    private Long id;
    private String name;
    private Integer ageGroup;
    private String contact;
    private String email;
    private String memo;
    private Gender gender;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static CreateCustomerResDto fromEntity(Customer customer) {
        return CreateCustomerResDto.builder()
            .id(customer.getId())
            .name(customer.getName())
            .ageGroup(customer.getAgeGroup())
            .contact(customer.getContact())
            .email(customer.getEmail())
            .memo(customer.getMemo())
            .gender(customer.getGender())
            .createdAt(customer.getCreatedAt())
            .updatedAt(customer.getUpdatedAt())
            .deletedAt(customer.getDeletedAt())
            .build();
    }

}
