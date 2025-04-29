package com.househub.backend.domain.crawlingProperty.entity;

import com.househub.backend.domain.crawlingProperty.enums.Direction;
import com.househub.backend.domain.crawlingProperty.enums.PropertyType;
import com.househub.backend.domain.crawlingProperty.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "crawling_properties")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CrawlingProperty {

    @Id
    private String crawlingPropertiesId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyType propertyType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private String province;

    @Column(nullable = false)
    private String city;

    private String dong;

    private String detailAddress;

    @Column(nullable = false)
    private Float area;

    private String floor;

    private String allFloors;

    private Float salePrice;

    private Float deposit;

    private Float monthlyRentFee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Direction direction;

    private String realEstateAgentId;

    private String realEstateAgentName;

    private String realEstateAgentContact;

    private String realEstateOfficeName;

    private String realEstateOfficeAddress;

    private Integer bathRoomCnt;

    private Integer roomCnt;

    @OneToMany(mappedBy = "crawlingProperty")
    private List<CrawlingPropertyTagMap> crawlingPropertyTagMaps = new ArrayList<>();
}
