package com.househub.backend.domain.tag.service;

import java.util.List;

import com.househub.backend.domain.tag.entity.Tag;

public interface TagStore {
	List<Tag> findAllById(List<Long> tagIds);
}
