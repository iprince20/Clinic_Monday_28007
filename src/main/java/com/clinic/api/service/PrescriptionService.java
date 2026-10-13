package com.clinic.api.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.clinic.api.model.Appointment;
import com.clinic.api.model.AppointmentStatus;
import com.clinic.api.model.Prescription;
import com.clinic.api.repository.AppointmentRepository;
import com.clinic.api.repository.PrescriptionRepository;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository,
            AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public PrescriptionSaveResult savePrescription(Long appointmentId, Prescription prescription) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (appointmentOpt.isEmpty()) {
            return new PrescriptionSaveResult(HttpStatus.NOT_FOUND,
                    "The appointment with that id does not exist");
        }

        Appointment appointment = appointmentOpt.get();
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            return new PrescriptionSaveResult(HttpStatus.BAD_REQUEST,
                    "Prescriptions are only for COMPLETED appointments");
        }

        prescription.setAppointment(appointment);
        prescription.setIssuedDate(LocalDate.now());
        prescriptionRepository.save(prescription);
        return new PrescriptionSaveResult(HttpStatus.CREATED, "Prescription saved successfully");
    }

    public List<Prescription> getPrescriptionsByAppointmentId(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "The appointment with that id does not exist"));
        return prescriptionRepository.findByAppointment(appointment);
    }

    public List<Prescription> getPrescriptionsByPatientSsNumber(String ssNumber) {
        return prescriptionRepository.findByPatientSsNumberOrderByIssuedDateDesc(ssNumber);
    }

    public static class PrescriptionSaveResult {
        private final HttpStatus status;
        private final String message;

        public PrescriptionSaveResult(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
