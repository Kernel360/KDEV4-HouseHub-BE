package com.househub.backend.domain.contract.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.contract.dto.UpdateContractReqDto;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.enums.ContractType;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.property.entity.Property;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contracts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("deleted_at IS NULL") // 조회 시 자동으로 deletedAt == null 조건
public class Contract {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "agentId", nullable = false)
	private Agent agent; // 담당 공인중개사

	@ManyToOne
	@JoinColumn(name = "customerId")
	private Customer customer; // 임차인 또는 매수인

	@ManyToOne
	@JoinColumn(name = "propertyId", nullable = false)
	private Property property;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ContractType contractType; // 거래 유형 (매매, 전세, 월세)

	private Long salePrice; // 매매가
	private Long jeonsePrice; // 전세가
	private Long monthlyRentFee; // 월세 금액
	private Long monthlyRentDeposit; // 월세 보증금

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ContractStatus status; // 상태 (계약 가능, 계약 중, 계약 완료, 계약 취소)

	private String memo; // 참고 설명

	private LocalDate startedAt; // 계약 시작일
	private LocalDate expiredAt; // 계약 만료일
	private LocalDate completedAt; // 계약 완료일시

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

	public void update(UpdateContractReqDto updateDto) {
		if (updateDto.getContractStatus() != null) {
			if(updateDto.getContractStatus() == ContractStatus.COMPLETED) {
				// 해당 매물 disable
//				property.getContracts().forEach(Contract::disable);
				property.disable();
			}
			if (updateDto.getContractStatus() != ContractStatus.COMPLETED) {
				this.completedAt = null; // 거래 완료 상태가 아닌 경우에는 null 로 설정
			}
			if (updateDto.getContractStatus() == ContractStatus.AVAILABLE) {
				this.startedAt = null;
				this.expiredAt = null;
			}
			this.status = updateDto.getContractStatus();
		}
		if (updateDto.getContractType() != null)
			this.contractType = updateDto.getContractType();
		if (updateDto.getMemo() != null)
			this.memo = updateDto.getMemo();

		// 가격 정보
		if (updateDto.getSalePrice() != null)
			this.salePrice = updateDto.getSalePrice();
		if (updateDto.getJeonsePrice() != null)
			this.jeonsePrice = updateDto.getJeonsePrice();
		if (updateDto.getMonthlyRentDeposit() != null)
			this.monthlyRentDeposit = updateDto.getMonthlyRentDeposit();
		if (updateDto.getMonthlyRentFee() != null)
			this.monthlyRentFee = updateDto.getMonthlyRentFee();

		// 날짜 정보
		if (updateDto.getStartedAt() != null)
			this.startedAt = updateDto.getStartedAt();
		if (updateDto.getExpiredAt() != null)
			this.expiredAt = updateDto.getExpiredAt();
		if (updateDto.getCompletedAt() != null)
			this.completedAt = updateDto.getCompletedAt();
		// if (updateDto.getActive() != null)
		// 	this.active = updateDto.getActive();
	}

	public void updateCustomer(Customer customer) {
		this.customer = customer;
	}

	// 삭제 메서드
	public void softDelete() {
		this.deletedAt = LocalDateTime.now();
	}
}
