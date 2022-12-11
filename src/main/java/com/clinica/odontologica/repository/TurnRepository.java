package com.clinica.odontologica.repository;

import com.clinica.odontologica.domain.Turn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface TurnRepository extends JpaRepository<Turn, Long> {
    //@Query("SELECT turn FROM Turn turn WHERE turn.dentist.registrationNumber = :name")
    public Optional<Turn> getByDentistRegistrationNumber(@Param("name") Long registrationNumber);

    //@Query("SELECT turn FROM Turn turn WHERE turn.patient.dni = ?1")
    public Optional<Turn> getByPatientDni(Long dni);
}
