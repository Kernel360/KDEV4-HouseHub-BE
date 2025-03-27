package com.househub.backend.domain.property.repository;

import com.househub.backend.domain.property.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    // 주소와 상세주소를 결합한 값으로 중복 체크
    boolean existsByRoadAddressAndDetailAddress(String roadAddress, String detailAddress);
}
