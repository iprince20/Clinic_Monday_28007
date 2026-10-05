package com.clinic.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.clinic.api.model.Doctor;
import com.clinic.api.model.Office;
import com.clinic.api.model.Specialization;
import com.clinic.api.repository.DoctorRepository;
import com.clinic.api.repository.OfficeRepository;
import com.clinic.api.repository.SpecializationRepository;

@Service
public class DoctorService {

	private final DoctorRepository doctorRepository;
	private final OfficeRepository officeRepository;
	private final SpecializationRepository specializationRepository;

	public DoctorService(DoctorRepository doctorRepository, OfficeRepository officeRepository,
			SpecializationRepository specializationRepository) {
		this.doctorRepository = doctorRepository;
		this.officeRepository = officeRepository;
		this.specializationRepository = specializationRepository;
	}

	public List<Doctor> findAll() {
		return doctorRepository.findAll();
	}

	public Optional<Doctor> findById(Long id) {
		return doctorRepository.findById(id);
	}

	public Doctor save(Doctor doctor) {
		doctor.setOffice(resolveOffice(doctor.getOffice()));
		doctor.setSpecializations(resolveSpecializations(doctor.getSpecializations()));
		return doctorRepository.save(doctor);
	}

	public Optional<Doctor> update(Long id, Doctor doctorDetails) {
		return doctorRepository.findById(id).map(doctor -> {
			doctor.setFirstName(doctorDetails.getFirstName());
			doctor.setLastName(doctorDetails.getLastName());
			doctor.setOffice(resolveOffice(doctorDetails.getOffice()));
			doctor.setSpecializations(resolveSpecializations(doctorDetails.getSpecializations()));
			return doctorRepository.save(doctor);
		});
	}

	public void deleteById(Long id) {
		doctorRepository.deleteById(id);
	}

	private Office resolveOffice(Office office) {
		if (office == null) {
			return null;
		}
		if (office.getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Office id is required");
		}
		return officeRepository.findById(office.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Office not found with id " + office.getId()));
	}

	private List<Specialization> resolveSpecializations(List<Specialization> specializations) {
		List<Specialization> resolved = new ArrayList<>();
		if (specializations == null) {
			return resolved;
		}
		for (Specialization specialization : specializations) {
			if (specialization == null || specialization.getId() == null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Specialization id is required");
			}
			resolved.add(specializationRepository.findById(specialization.getId())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
							"Specialization not found with id " + specialization.getId())));
		}
		return resolved;
	}
}