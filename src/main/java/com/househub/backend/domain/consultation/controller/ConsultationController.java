package com.househub.backend.domain.consultation.controller;

import com.househub.backend.domain.consultation.dto.ConsultationReqDto;
import com.househub.backend.domain.consultation.entity.Consultation;
import com.househub.backend.domain.consultation.service.ConsultationService;
//import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    private final ConsultationService consultationService;

    @PostMapping("")
    public Consultation createConsultation(
//        @Valid
        @RequestBody ConsultationReqDto consultationReqDto
    ) {
        return consultationService.create(consultationReqDto);
    }

//    @GetMapping("")
//    public void findAllConsultations() {
//
//    }
//
//    @GetMapping("/{consultationsId}")
//    public void findOneConsultation() {
//
//    }
//
//    @PutMapping("/{consultationsId}")
//    public void updateConsultation() {
//
//    }
//
//    @DeleteMapping("/{consultationsId")
//    public void deleteConsultation() {
//
//    }

}
