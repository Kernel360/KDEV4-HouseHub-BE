package com.househub.backend.domain.crawlingProperty.repository;

import com.househub.backend.domain.crawlingProperty.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
