package com.househub.backend.domain.contract.entity;

import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.enums.ContractType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contracts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
