package com.clinica.odontologica.repository;

import com.clinica.odontologica.model.domain.Turn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TurnRepository extends JpaRepository<Turn, Long> {
    // @Query("SELECT turn FROM Turn turn WHERE turn.dentist.registrationNumber =
    // ?1")
    public Optional<List<Turn>> getByDentistRegistrationNumber(Long registrationNumber);

    // @Query("SELECT turn FROM Turn turn WHERE turn.patient.dni = ?1")
    public Optional<List<Turn>> getByPatientDni(Long dni);
}
