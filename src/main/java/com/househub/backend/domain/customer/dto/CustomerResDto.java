package com.househub.backend.domain.customer.dto;

import com.househub.backend.domain.customer.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResDto {

    private String name;
    private String ageGroup;
    private String contact;
    private String email;
    private String memo;
    private String gender;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static CustomerResDto toDto(Customer entity){
        return CustomerResDto.builder()
                .name(entity.getName())
                .ageGroup(entity.getAgeGroup())
                .contact(entity.getContact())
                .email(entity.getEmail())
                .memo(entity.getMemo())
                .gender(entity.getGender())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Customer toEntity(){
        return Customer.builder()
                .name(this.name)
                .ageGroup(this.ageGroup)
                .contact(this.contact)
                .email(this.email)
                .memo(this.memo)
                .gender(this.gender)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .deletedAt(this.deletedAt)
                .build();
    }

}
