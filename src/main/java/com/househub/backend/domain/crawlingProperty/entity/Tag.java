package com.househub.backend.domain.crawlingProperty.entity;

import java.util.ArrayList;
import java.util.List;

import com.househub.backend.domain.customer.entity.CustomerTagMap;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tags")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String value;

    @OneToMany(mappedBy = "tag")
    @Builder.Default
    private List<CustomerTagMap> customerTagMaps = new ArrayList<>();
}
