package com.househub.backend.domain.property.service.impl;

import java.util.List;

import com.househub.backend.domain.contract.dto.BasicContractReqDto;
import com.househub.backend.domain.contract.service.ContractStore;
import com.househub.backend.domain.property.service.PropertyTagMapStore;
import com.househub.backend.domain.property.validator.PropertyValidator;
import com.househub.backend.domain.tag.entity.Tag;
import com.househub.backend.domain.tag.service.TagReader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.service.CustomerReader;
import com.househub.backend.domain.property.dto.CreatePropertyResDto;
import com.househub.backend.domain.property.dto.FindPropertyDetailResDto;
import com.househub.backend.domain.property.dto.FindPropertyResDto;
import com.househub.backend.domain.property.dto.PropertyListResDto;
import com.househub.backend.domain.property.dto.CreatePropertyReqDto;
import com.househub.backend.domain.property.dto.PropertySearchDto;
import com.househub.backend.domain.property.dto.UpdatePropertyReqDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.service.PropertyReader;
import com.househub.backend.domain.property.service.PropertyService;
import com.househub.backend.domain.property.service.PropertyStore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

	private final ContractStore contractStore;
	private final CustomerReader customerReader;
	private final PropertyStore propertyStore;
	private final PropertyReader propertyReader;
	private final PropertyTagMapStore propertyTagMapStore;
	private final TagReader tagReader;
	private final PropertyValidator propertyValidator;

	/**
	 * 매물 등록
	 * @param dto 매물 등록 정보 DTO
	 * @param agentDto 공인중개사 DTO
	 * @return 등록된 매물 id 를 반환하는 DTO
	 */
	@Transactional
	@Override
	public CreatePropertyResDto createProperty(CreatePropertyReqDto dto, AgentResDto agentDto) {
		// 로그인한 공인중개사 조회
		Agent agent = agentDto.toEntity();
		// 의뢰인(임대인 또는 매도인) 존재 여부 확인
		Customer customer = customerReader.findByIdOrThrow(dto.getCustomerId(), agentDto.getId());
		// 동일한 고객이 동일한 주소의 매물을 등록할 수 없도록 처리
		propertyValidator.validateUniqueAddressForCustomer(dto.getRoadAddress(), dto.getJibunAddress(), dto.getCustomerId());
		// dto -> entity
		Property property = dto.toEntity(customer, agent);
		// db에 저장
		propertyStore.create(property);
		List<Tag> tags = tagReader.findAllById(dto.getTagIds());
		propertyStore.addTag(property, tags);
		// 계약 등록 - '계약 가능' 상태로 등록 (계약자 없음)
		if(dto.getContract() != null) {
			BasicContractReqDto contractReqDto = dto.getContract();
			contractStore.create(contractReqDto.toEntity(property, agent));
			property.enable();
		}
		// 응답 객체 리턴
		return new CreatePropertyResDto(property.getId());
	}

	/**
	 * 매물 상세 조회 - 해당 매물의 계약 리스트도 함께 조회
	 * @param id 매물 ID
	 * @param agentDto 공인중개사 DTO
	 * @return 매물 상세 정보 응답 DTO
	 */
	@Transactional(readOnly = true)
	@Override
	public FindPropertyDetailResDto findProperty(Long id, AgentResDto agentDto) {
		// 매물 조회
		Property property = propertyReader.findByIdOrThrow(id, agentDto.getId());
		// entity -> dto
		return FindPropertyDetailResDto.fromEntity(property);
	}

	/**
	 * 매물 전체 조회 (검색 포함)
	 * @param searchDto 매물 검색 조건 DTO
	 * @param pageable 페이지네이션 정보
	 * @param agentDto 공인중개사 DTO
	 * @return 매물 기본 정보 응답 DTO LIST
	 */
	@Transactional(readOnly = true)
	@Override
	public PropertyListResDto findProperties(PropertySearchDto searchDto, Pageable pageable, AgentResDto agentDto) {
		// 페이지네이션, 검색 필터링 적용하여 매물 조회
		Page<Property> propertyList = propertyReader.findPageBySearchDto(searchDto, pageable, agentDto.getId());
		// 매물 엔티티를 dto 로 변환하여 리스트로 반환
		return PropertyListResDto.fromPage(propertyList.map(FindPropertyResDto::toDto));
	}

	/**
	 * 특정 고객의 매물 전체 조회 (검색 포함)
	 * @param customerId 고객 Id
	 * @param pageable 페이지네이션 정보
	 * @return 매물 기본 정보 응답 DTO LIST
	 */
	@Transactional(readOnly = true)
	@Override
	public List<Property> findPropertiesByCustomer(Long customerId, Pageable pageable, Long agentId) {
		// 페이지네이션, 검색 필터링 적용하여 매물 조회
		return propertyReader.searchPropertiesByCustomer(
			agentId,
			customerId
		);
	}

	/**
	 * 매물 정보 수정
	 * @param propertyId 매물 id
	 * @param updateDto 수정된 매물 정보 DTO
	 */
	@Transactional
	@Override
	public void updateProperty(Long propertyId, UpdatePropertyReqDto updateDto, AgentResDto agentDto) {
		// 의뢰인(임대인 또는 매도인) 존재 여부 확인
		Customer customer = null;
		// 의뢰인을 수정한 경우
		if(updateDto.getCustomerId() != null) {
			customer = customerReader.findByIdOrThrow(updateDto.getCustomerId(), agentDto.getId());
		}
		// 매물 조회
		Property property = propertyReader.findByIdOrThrow(propertyId, agentDto.getId());
		// 매물의 고객 ID나 매물 주소가 변경된 경우, 동일한 고객이 동일한 주소의 매물을 등록할 수 없도록 처리
		propertyValidator.validateUniqueAddressOnUpdate(updateDto, property);
		if(updateDto.getTagIds() != null) {
			propertyTagMapStore.deleteByPropertyId(propertyId);
			List<Tag> tags = tagReader.findAllById(updateDto.getTagIds());
			propertyStore.addTag(property, tags);
		}
		propertyStore.update(updateDto, property, customer);
	}

	/**
	 * 매물 삭제
	 * @param propertyId 매물 ID
	 * @param agentDto 공인중개사 DTO
	 */
	@Transactional
	@Override
	public void deleteProperty(Long propertyId, AgentResDto agentDto) {
		// 매물 조회
		Property property = propertyReader.findByIdOrThrow(propertyId, agentDto.getId());
		// 매물 소프트 삭제
		propertyStore.delete(property);
	}
}
