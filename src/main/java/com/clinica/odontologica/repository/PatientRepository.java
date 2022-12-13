package com.clinica.odontologica.repository;

import com.clinica.odontologica.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Transactional
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    public Optional<Patient> getByDni(Long dni);
    @Modifying
    public void deleteByDni(Long dni);


}
