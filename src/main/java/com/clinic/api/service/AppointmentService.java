package com.clinic.api.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.clinic.api.model.Appointment;
import com.clinic.api.model.AppointmentStatus;
import com.clinic.api.model.Doctor;
import com.clinic.api.model.Patient;
import com.clinic.api.repository.AppointmentRepository;
import com.clinic.api.repository.DoctorRepository;
import com.clinic.api.repository.PatientRepository;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    public List<Appointment> findByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatusOrderByAppointmentDateAsc(status);
    }

    public List<Appointment> findBetween(LocalDate start, LocalDate end) {
        return appointmentRepository.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }

    public List<Object[]> countByStatus() {
        return appointmentRepository.countAppointmentsByStatus();
    }

    public Page<Appointment> findAll(Pageable pageable) {
        return appointmentRepository.findAll(pageable);
    }

    @Transactional
    public int cancelDay(Long doctorId, LocalDate date) {
        return appointmentRepository.cancelAppointmentsForDoctorOnDate(doctorId, date,
                AppointmentStatus.COMPLETED, AppointmentStatus.CANCELLED);
    }

    @Transactional
    public int deleteCancelledBefore(LocalDate date) {
        return appointmentRepository.deleteAppointmentsBeforeDate(AppointmentStatus.CANCELLED, date);
    }

    public Appointment save(Appointment appointment) {
        resolveReferences(appointment);
        if (appointment.getAppointmentDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment date is required");
        }
        if (appointment.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment date cannot be in the past");
        }
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        if (appointmentRepository.existsByDoctorAndAppointmentDateAndStatusNot(
                appointment.getDoctor(), appointment.getAppointmentDate(), AppointmentStatus.CANCELLED)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Doctor is already booked on that date");
        }
        return appointmentRepository.save(appointment);
    }

    public Optional<Appointment> update(Long id, Appointment appointmentDetails) {
        return appointmentRepository.findById(id).map(appointment -> {
            resolveReferences(appointmentDetails);
            if (appointmentDetails.getAppointmentDate() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment date is required");
            }
            appointment.setDoctor(appointmentDetails.getDoctor());
            appointment.setPatient(appointmentDetails.getPatient());
            appointment.setAppointmentDate(appointmentDetails.getAppointmentDate());
            appointment.setStatus(appointmentDetails.getStatus() == null
                    ? AppointmentStatus.SCHEDULED
                    : appointmentDetails.getStatus());
            return appointmentRepository.save(appointment);
        });
    }

    public void deleteById(Long id) {
        appointmentRepository.deleteById(id);
    }

    private void resolveReferences(Appointment appointment) {
        Doctor doctor = appointment.getDoctor();
        if (doctor == null || doctor.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor id is required");
        }
        appointment.setDoctor(doctorRepository.findById(doctor.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Doctor not found with id " + doctor.getId())));

        Patient patient = appointment.getPatient();
        if (patient == null || patient.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patient id is required");
        }
        appointment.setPatient(patientRepository.findById(patient.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Patient not found with id " + patient.getId())));
    }
}