package com.househub.backend.domain.property.entity;

import com.househub.backend.domain.tag.entity.Tag;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "property_tag_map", uniqueConstraints = {
        @UniqueConstraint(name = "UK_property_tag", columnNames = {"property_id", "tag_id"})
})
@Getter
@Builder
public class PropertyTagMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;
}
