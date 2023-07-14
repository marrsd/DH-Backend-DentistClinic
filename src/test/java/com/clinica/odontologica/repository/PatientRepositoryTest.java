package com.clinica.odontologica.repository;

import com.clinica.odontologica.model.domain.Address;
import com.clinica.odontologica.model.domain.Patient;
import com.clinica.odontologica.model.domain.auth.ERole;
import com.clinica.odontologica.model.domain.auth.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    PatientRepository patientRepository;

    private Patient patient;

    private User user;

    private Address address;

    @BeforeEach
    public void init() {
        user = new User(1L, "userabc", "1234", ERole.ADMIN, true, false);

        address = new Address();
        address.setId(1L);
        address.setNumber(12);
        address.setLocality("North");
        address.setProvince("Campestre");
        address.setStreet("14A");

        patient = new Patient();
        patient.setId(1L);
        patient.setDni(1234L);
        patient.setFirstname("Pablito");
        patient.setLastname("Clavito");
        patient.setDateHourAdmission(LocalDateTime.now());
        patient.setUser(user);
        patient.setAddress(address);
    }

    @AfterEach
    public void destroyAll() {
        patientRepository.deleteAll();
    }

    @Test
    public void createPatientTest() {
        Patient patient1 = patientRepository.save(patient);

        assertNotNull(patient1);
    }

    @Test
    public void getAllPatientTest() {
        patientRepository.save(patient);

        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setDni(7483L);
        patient2.setFirstname("Lucy");
        patient2.setLastname("Ortiz");
        patient2.setDateHourAdmission(LocalDateTime.now());
        patient2.setUser(user);
        patient2.setAddress(address);

        patientRepository.save(patient2);

        List<Patient> patientList = patientRepository.findAll();

        assertEquals(patientList.size(), 2);
    }

    @Test
    public void getPatientById() {
        Patient patient1 = patientRepository.save(patient);

        Optional<Patient> patient2 = patientRepository.findById(patient1.getId());

        assertEquals(patient1, patient2.get());
    }

    @Test
    public void getPatientByDni() {
        Patient patient1 = patientRepository.save(patient);

        Optional<Patient> patient2 = patientRepository.getByDni(patient1.getDni());

        assertEquals(patient1, patient2.get());
    }

    @Test
    public void deletePatientById() {
        Patient patient1 = patientRepository.save(patient);

        patientRepository.deleteById(patient1.getId());
        Optional<Patient> patient2 = patientRepository.findById(patient1.getId());

        assertEquals(Optional.empty(), patient2);
    }

    @Test
    public void deletePatientByDni() {
        Patient patient1 = patientRepository.save(patient);

        patientRepository.deleteByDni(patient1.getDni());
        Optional<Patient> patient2 = patientRepository.findById(patient1.getDni());

        assertEquals(Optional.empty(), patient2);
    }
}