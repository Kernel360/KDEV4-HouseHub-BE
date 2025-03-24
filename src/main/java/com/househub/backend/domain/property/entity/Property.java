package com.househub.backend.domain.property.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Entity
@Table(name = "properties")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyId; // 매물 고유 식별자 (PK)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyType propertyType; // 매물 유형 (아파트, 오피스텔, 상가, 원룸, 투룸, 빌라)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType; // 거래 유형 (매매, 전세, 월세)

    private String memo; // 참고 설명

    private String province; // 도, 특별시, 광역시
    private String city; // 시/군/구
    private String dong; // 읍/면/동
    private String detailAddress; // 상세 주소
    private String roadAddress; // 전체 도로명 주소

    private Integer salePrice; // 매매가
    private Integer jeonsePrice; // 전세 금액
    private Integer monthlyRentDeposit; // 월세 보증금
    private Integer monthlyRentFee; // 월세 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyStatus status; // 상태 (판매 중, 판매 완료)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 등록일시

    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정일시

    private LocalDateTime deletedAt; // 삭제일시 (소프트 삭제)

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude; // 위도

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude; // 경도

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    // 내부 Enum 선언
    public enum PropertyType {
        APARTMENT, VILLA, OFFICETEL, COMMERCIAL, ONE_ROOM, TWO_ROOM
    }

    public enum TransactionType {
        SALE, JEONSE, MONTHLY_RENT
    }

    public enum PropertyStatus {
        ON_SALE, SOLD_OUT
    }
}
