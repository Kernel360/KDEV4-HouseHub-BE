package com.househub.backend.domain.crawlingProperty.service;

import com.househub.backend.domain.crawlingProperty.entity.Tag;

import java.util.List;

public interface TagReader {
    List<Tag> findAllByIds(List<Long> ids);
}
