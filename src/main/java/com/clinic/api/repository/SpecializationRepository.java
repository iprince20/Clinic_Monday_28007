package com.clinic.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.api.model.Specialization;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
}