package com.clinic.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.clinic.api.model.Patient;
import com.clinic.api.repository.PatientRepository;

@Service
public class PatientService {

	private final PatientRepository patientRepository;

	public PatientService(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}

	public List<Patient> findAll() {
		return patientRepository.findAll();
	}

	public Optional<Patient> findById(Long id) {
		return patientRepository.findById(id);
	}

	public List<Patient> findByLastNameIgnoreCase(String lastName) {
		return patientRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
	}

	public Patient save(Patient patient) {
		return patientRepository.save(patient);
	}

	public Optional<Patient> update(Long id, Patient patientDetails) {
		return patientRepository.findById(id).map(patient -> {
			patient.setFirstName(patientDetails.getFirstName());
			patient.setLastName(patientDetails.getLastName());
			patient.setEmail(patientDetails.getEmail());
			patient.setPhone(patientDetails.getPhone());
			return patientRepository.save(patient);
		});
	}

	public void deleteById(Long id) {
		patientRepository.deleteById(id);
	}
}