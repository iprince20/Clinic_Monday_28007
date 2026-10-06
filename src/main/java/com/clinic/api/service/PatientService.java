package com.clinic.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.clinic.api.model.Patient;
import com.clinic.api.repository.DoctorRepository;
import com.clinic.api.repository.PatientRepository;

@Service
public class PatientService {

	private final PatientRepository patientRepository;
	private final DoctorRepository doctorRepository;

	public PatientService(PatientRepository patientRepository, DoctorRepository doctorRepository) {
		this.patientRepository = patientRepository;
		this.doctorRepository = doctorRepository;
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

	public List<Patient> findByDoctorId(Long doctorId) {
		if (!doctorRepository.existsById(doctorId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The doctor with that id does not exist");
		}
		return patientRepository.findDistinctPatientsByDoctorId(doctorId);
	}

	public List<Patient> findFrequent(long min) {
		return patientRepository.findFrequentPatients(min);
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