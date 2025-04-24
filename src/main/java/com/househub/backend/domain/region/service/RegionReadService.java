package com.househub.backend.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.househub.backend.domain.region.dto.RegionOptionDto;
import com.househub.backend.domain.region.repository.RegionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegionReadService {

	private final RegionRepository regionRepository;

	public List<RegionOptionDto> getProvinces() {
		return regionRepository.findDistinctProvinces();
	}

	public List<RegionOptionDto> getCities(String province) {
		return regionRepository.findCitiesByProvince(province);
	}

	public List<RegionOptionDto> getDongs(String province, String city) {
		return regionRepository.findDongsByProvinceAndCity(province, city);
	}
}

