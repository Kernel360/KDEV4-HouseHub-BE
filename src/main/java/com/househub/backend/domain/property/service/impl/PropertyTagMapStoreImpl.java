package com.househub.backend.domain.property.service.impl;

import com.househub.backend.domain.property.repository.PropertyTagMapRepository;
import com.househub.backend.domain.property.service.PropertyTagMapStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PropertyTagMapStoreImpl implements PropertyTagMapStore {

    private final PropertyTagMapRepository propertyTagMapRepository;

    @Override
    public void deleteByPropertyId(Long propertyId) {
        propertyTagMapRepository.deleteByPropertyId(propertyId);
    }
}
