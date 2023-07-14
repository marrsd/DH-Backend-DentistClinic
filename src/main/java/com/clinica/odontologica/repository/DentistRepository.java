package com.clinica.odontologica.repository;

import com.clinica.odontologica.model.domain.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface DentistRepository extends JpaRepository<Dentist, Long> {

    public Optional<Dentist> getByRegistrationNumber(Long registrationNumber);

    public Optional<Dentist> getByFirstnameAndLastname(String firstname, String Lastname);

    @Modifying
    public void deleteByRegistrationNumber(Long registrationNumber);

}
