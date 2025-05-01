package com.househub.backend.domain.customer.entity;

import com.househub.backend.domain.crawlingProperty.entity.Tag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_tag_map", uniqueConstraints = {
	@UniqueConstraint(name = "UK_customer_tag", columnNames = {"customer_id", "tag_id"})
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerTagMap {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Customer와 다대일 관계 (N:1)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	// Tag와 다대일 관계 (N:1)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_id", nullable = false)
	private Tag tag;
}
