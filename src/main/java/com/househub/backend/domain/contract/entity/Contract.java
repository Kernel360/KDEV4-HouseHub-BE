package com.househub.backend.domain.contract.entity;

import com.househub.backend.domain.contract.dto.ContractReqDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.enums.ContractType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "contracts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at IS NULL") // 조회 시 자동으로 deletedAt == null 조건
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contractId;

//    @Column(name = "customer_id", nullable = false)
//    private Long customerId;

    @ManyToOne
    @JoinColumn(name = "propertyId", nullable = false)
    private Property property;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractType contractType; // 거래 유형 (매매, 전세, 월세)

    private Long salePrice; // 매매가
    private Long jeonsePrice; // 전세가
    private Integer monthlyRentFee; // 월세 금액
    private Integer monthlyRentDeposit; // 월세 보증금

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status; // 상태 (판매 중, 판매 완료)

    private String memo; // 참고 설명 - 월세 계약 기간 등

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 등록일시

    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정일시

    private LocalDateTime deletedAt; // 삭제일시 (소프트 삭제)

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // 수정 메서드 (setter 대신 사용)
    public void updateProperty(ContractReqDto updatDto) {
        if (updatDto.getContractType() != null) this.contractType = updatDto.getContractType();
        if (updatDto.getMemo() != null) this.memo = updatDto.getMemo();
        if (updatDto.getSalePrice() != null) this.salePrice = updatDto.getSalePrice();
        if (updatDto.getJeonsePrice() != null) this.jeonsePrice = updatDto.getJeonsePrice();
        if (updatDto.getMonthlyRentDeposit() != null) this.monthlyRentDeposit = updatDto.getMonthlyRentDeposit();
        if (updatDto.getMonthlyRentFee() != null) this.monthlyRentFee = updatDto.getMonthlyRentFee();
        this.updatedAt = LocalDateTime.now();
    }

    // 삭제 메서드
    public void deleteContract() {
        this.deletedAt = LocalDateTime.now();
    }
}
