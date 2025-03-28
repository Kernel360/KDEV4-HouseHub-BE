package com.househub.backend.domain.customer.entity;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer ageGroup;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void update(CreateCustomerReqDto reqDto) {
        this.name = reqDto.getName();
        this.email = reqDto.getEmail();
        this.ageGroup = reqDto.getAgeGroup();
        this.memo = reqDto.getMemo();
        this.contact = reqDto.getContact();
        this.gender = reqDto.getGender();
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() {
        this.deletedAt = null;
    }

    public CreateCustomerResDto toDto() {
        return CreateCustomerResDto.builder()
                .name(this.getName())
                .ageGroup(this.getAgeGroup())
                .contact(this.getContact())
                .email(this.getEmail())
                .memo(this.getMemo())
                .gender(this.getGender())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .deletedAt(this.getDeletedAt())
                .build();
    }
}
