package com.clinic.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.api.model.Prescription;
import com.clinic.api.service.PrescriptionService;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> savePrescription(
            @RequestParam("appointmentId") Long appointmentId,
            @RequestBody Prescription prescription) {
        PrescriptionService.PrescriptionSaveResult result =
                prescriptionService.savePrescription(appointmentId, prescription);
        return ResponseEntity.status(result.getStatus()).body(result.getMessage());
    }

    @GetMapping("/appointment/{appointmentId}")
    public List<Prescription> getPrescriptionsByAppointment(@PathVariable Long appointmentId) {
        return prescriptionService.getPrescriptionsByAppointmentId(appointmentId);
    }

    @GetMapping("/patient/{ssNumber}")
    public List<Prescription> getPrescriptionsByPatient(@PathVariable String ssNumber) {
        return prescriptionService.getPrescriptionsByPatientSsNumber(ssNumber);
    }
}
