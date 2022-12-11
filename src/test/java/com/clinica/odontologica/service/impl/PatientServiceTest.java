package com.clinica.odontologica.service.impl;

import com.clinica.odontologica.dto.AddressDTO;
import com.clinica.odontologica.dto.PatientDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    private PatientDTO patient;
    private AddressDTO address;

    @BeforeEach
    public void init() {
        address = new AddressDTO();
        address.setStreet("32A");
        address.setNumber(12);
        address.setLocality("Green");
        address.setProvince("provincia");

        patient = new PatientDTO();
        patient.setDni(1234L);
        patient.setFirstname("Pablito");
        patient.setLastname("Clavito");
        patient.setAddress(address);
    }

    @Test
    @Order(1)
    public void createPatientTest() throws Exception {
        patient.setDni(234L);
        PatientDTO p = patientService.create(patient);
        assertNotNull(p);
    }

    @Test
    @Order(2)
    public void getAllPatientsTest() throws Exception{
        patient.setDni(3456L);
        patientService.create(patient);

        List<PatientDTO> patients = patientService.getAll();
        assertNotEquals(patients.size(), 0);

    }

    @Test
    @Order(3)
    public void getPatientByIdTest() throws Exception {
        patient.setDni(1664L);
        PatientDTO d = patientService.create(patient);

        PatientDTO patient = patientService.getById(d.getId());
        assertEquals(patient.getDni(), 1664L);

    }

    @Test
    @Order(4)
    public void updatePatientTest() throws Exception {
        String apellido = "Lopez";

        patient.setDni(17334L);
        PatientDTO p = patientService.create(patient);

        p.setLastname(apellido);

        patientService.update(p);
        assertEquals(p.getLastname(), apellido);
    }

    @Test
    @Order(5)
    public void deleteDentistTest() throws Exception {
        patient.setDni(5345L);
        PatientDTO p = patientService.create(patient);

        IllegalArgumentException exc = assertThrows(IllegalArgumentException.class, () ->  patientService.delete(-1L));

        assertEquals(exc.getMessage(), "Id can´t be negative");
    }

}