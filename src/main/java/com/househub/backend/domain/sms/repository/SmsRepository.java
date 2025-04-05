package com.househub.backend.domain.sms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.househub.backend.domain.agent.entity.RealEstate;
import com.househub.backend.domain.sms.dto.SendSmsResDto;
import com.househub.backend.domain.sms.entity.Sms;

public interface SmsRepository extends JpaRepository<Sms, Long> {

	List<Sms> getAllSmsByRealEstate(RealEstate realEstate);

	SendSmsResDto findByIdAndRealEstate(Long id, RealEstate realEstate);
}
