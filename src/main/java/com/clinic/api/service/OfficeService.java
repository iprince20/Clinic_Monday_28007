package com.clinic.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.clinic.api.model.Office;
import com.clinic.api.repository.OfficeRepository;

@Service
public class OfficeService {

	private final OfficeRepository officeRepository;

	public OfficeService(OfficeRepository officeRepository) {
		this.officeRepository = officeRepository;
	}

	public List<Office> findAll() {
		return officeRepository.findAll();
	}

	public List<Object[]> findBusiestOffice() {
		return officeRepository.findBusiestOffice(PageRequest.of(0, 1));
	}

	public Optional<Office> findById(Long id) {
		return officeRepository.findById(id);
	}

	public Office save(Office office) {
		return officeRepository.save(office);
	}

	public Optional<Office> update(Long id, Office officeDetails) {
		return officeRepository.findById(id).map(office -> {
			office.setName(officeDetails.getName());
			office.setLocation(officeDetails.getLocation());
			office.setOfficeNumber(officeDetails.getOfficeNumber());
			return officeRepository.save(office);
		});
	}

	public void deleteById(Long id) {
		officeRepository.deleteById(id);
	}
}