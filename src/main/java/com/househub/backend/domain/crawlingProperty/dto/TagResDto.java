package com.househub.backend.domain.crawlingProperty.dto;

import com.househub.backend.domain.crawlingProperty.entity.Tag;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TagResDto {

    private Long tagId;
    private String type;
    private String value;

    public static TagResDto fromEntity(Tag tag) {
        return TagResDto.builder()
                .tagId(tag.getTagId())
                .type(tag.getType())
                .value(tag.getValue())
                .build();
    }
}
