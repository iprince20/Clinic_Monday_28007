package com.clinic.api.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clinic.api.model.Doctor;
import com.clinic.api.model.Office;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

	@Query("SELECT DISTINCT d FROM Doctor d JOIN d.specializations s WHERE LOWER(s.name) = LOWER(:name)")
	List<Doctor> findBySpecializationName(@Param("name") String name);

	@Query("SELECT d FROM Doctor d WHERE d.office IS NULL ORDER BY d.lastName ASC")
	List<Doctor> findWithoutOffice();

	@Query("SELECT o.name, o.officeNumber, COUNT(a) "
			+ "FROM Appointment a JOIN a.doctor d JOIN d.office o "
			+ "GROUP BY o.name, o.officeNumber ORDER BY COUNT(a) DESC")
	List<Object[]> findBusiestOffice(Pageable pageable);

	@Query("SELECT o FROM Office o WHERE NOT EXISTS (SELECT d FROM Doctor d WHERE d.office = o) ORDER BY o.officeNumber ASC")
	List<Office> findFreeOffices();
}