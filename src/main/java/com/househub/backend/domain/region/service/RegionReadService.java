package com.househub.backend.domain.region.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.househub.backend.domain.region.repository.RegionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegionReadService {
	private final RegionRepository regionRepository;

	public List<String> getProvinces() {
		return regionRepository.findDistinctProvinces();
	}

	public List<String> getCities(String province) {
		return regionRepository.findCitiesByProvince(province);
	}

	public List<String> getDongs(String province, String city) {
		return regionRepository.findDongsByProvinceAndCity(province, city);
	}
}
