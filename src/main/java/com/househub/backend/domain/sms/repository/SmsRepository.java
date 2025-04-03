package com.househub.backend.domain.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.househub.backend.domain.sms.entity.Sms;

public interface SmsRepository extends JpaRepository<Sms, Long> {

}
