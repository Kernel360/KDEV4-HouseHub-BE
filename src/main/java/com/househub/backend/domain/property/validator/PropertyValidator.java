package com.househub.backend.domain.property.validator;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.domain.property.dto.UpdatePropertyReqDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PropertyValidator {

    private final PropertyRepository propertyRepository;

    public void validateUniqueAddressForCustomer(String roadAddress, String detailAddress, Long customerId) {
        boolean isExist = propertyRepository.existsByRoadAddressAndDetailAddressAndCustomerId(roadAddress, detailAddress, customerId);
        if (isExist) {
            throw new AlreadyExistsException("해당 고객이 동일 주소로 등록한 매물이 존재합니다.", "PROPERTY_ALREADY_EXISTS");
        }
    }

    public void validateUniqueAddressOnUpdate(UpdatePropertyReqDto updateDto, Property property) {
        if (isAddressChanged(updateDto, property) || isCustomerChanged(updateDto, property)) {
            validateUniqueAddressForCustomer(
                    updateDto.getRoadAddress(),
                    updateDto.getDetailAddress(),
                    updateDto.getCustomerId()
            );
        }
    }

    private boolean isAddressChanged(UpdatePropertyReqDto updateDto, Property property) {
        return (updateDto.getRoadAddress() != null && !updateDto.getRoadAddress().equals(property.getRoadAddress())) ||
                (updateDto.getDetailAddress() != null && !updateDto.getDetailAddress().equals(property.getDetailAddress()));
    }

    private boolean isCustomerChanged(UpdatePropertyReqDto updateDto, Property property) {
        return updateDto.getCustomerId() != null && !updateDto.getCustomerId().equals(property.getCustomer().getId());
    }
}
