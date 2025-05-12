package com.househub.backend.domain.tag.service;

import java.util.List;

import com.househub.backend.domain.tag.entity.Tag;

public interface TagReader {
	List<Tag> findAllById(List<Long> tagIds);
}
